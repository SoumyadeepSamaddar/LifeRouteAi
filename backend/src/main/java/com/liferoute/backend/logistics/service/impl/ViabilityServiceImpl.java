package com.liferoute.backend.logistics.service.impl;

import com.liferoute.backend.logistics.dto.RouteOptionDTO;
import com.liferoute.backend.logistics.dto.ViabilityRequestDTO;
import com.liferoute.backend.logistics.dto.ViabilityResponseDTO;
import com.liferoute.backend.logistics.service.MapsClient;
import com.liferoute.backend.logistics.service.ViabilityService;
import com.liferoute.backend.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViabilityServiceImpl implements ViabilityService {
    private final MapsClient mapsClient;
    private final SystemSettingService systemSettingService;

    @Override
    public ViabilityResponseDTO evaluateCandidate(ViabilityRequestDTO request) {
        String organType = normalizeOrgan(request.getOrganType());
        double safeHours = getSafeHours(organType);
        double handoverBufferMinutes = systemSettingService.getDoubleValue("shelflife.handover_buffer_mins", 30d);
        double elapsed = request.getElapsedSinceRetrievalMinutes() == null ? 0d : request.getElapsedSinceRetrievalMinutes();
        double trafficPenalty = getTrafficPenalty(request.getTrafficLevel());
        double hazardPenalty = request.getHazardPenaltyMinutes() == null ? 0d : request.getHazardPenaltyMinutes();

        List<RouteOptionDTO> routeOptions = new ArrayList<>();
        List<String> modes = List.of("ROAD", "AIR", "TRAIN");
        double best = Double.MAX_VALUE;
        String bestMode = "ROAD";

        for (String mode : modes) {
            if (!isModeEligible(mode, request)) {
                continue;
            }

            MapsClient.RouteEstimate estimate = mapsClient.estimateRoute(
                    request.getDonorLatitude(), request.getDonorLongitude(),
                    request.getRecipientLatitude(), request.getRecipientLongitude(),
                    mode
            );

            double totalTime = estimate.durationMinutes() + trafficPenalty + hazardPenalty + handoverBufferMinutes;
            double buffer = (safeHours * 60) - (elapsed + totalTime);
            boolean viable = elapsed + totalTime <= safeHours * 60;

            var option = RouteOptionDTO.builder()
                    .mode(mode)
                    .distanceKm(Math.round(estimate.distanceKm() * 10.0) / 10.0)
                    .etaMinutes((double) estimate.durationMinutes())
                    .totalTimeMinutes(totalTime)
                    .remainingBufferMinutes(buffer)
                    .viable(viable)
                    .reason(viable ? "Within organ safety window" : "Travel exceeds safe time window")
                    .isSelected(false)
                    .build();

            routeOptions.add(option);
            if (totalTime < best) {
                best = totalTime;
                bestMode = mode;
            }
        }

        if (routeOptions.isEmpty()) {
            routeOptions.add(RouteOptionDTO.builder()
                    .mode("ROAD")
                    .distanceKm(0d)
                    .etaMinutes(0d)
                    .totalTimeMinutes(Double.MAX_VALUE)
                    .remainingBufferMinutes(-1d)
                    .viable(false)
                    .reason("No viable transport mode available")
                    .isSelected(true)
                    .build());
            bestMode = "ROAD";
        }

        for (RouteOptionDTO option : routeOptions) {
            if (option.getMode().equals(bestMode)) {
                option.setIsSelected(true);
            }
        }

        double totalTimeMinutes = routeOptions.stream()
                .filter(RouteOptionDTO::getViable)
                .map(RouteOptionDTO::getTotalTimeMinutes)
                .min(Comparator.naturalOrder())
                .orElse(best);

        double remainingBufferMinutes = (safeHours * 60) - (elapsed + totalTimeMinutes);
        boolean viable = elapsed + totalTimeMinutes <= safeHours * 60;

        return ViabilityResponseDTO.builder()
                .viable(viable)
                .totalTimeMinutes(totalTimeMinutes)
                .remainingBufferMinutes(remainingBufferMinutes)
                .safeHours(safeHours)
                .elapsedSinceRetrievalMinutes(elapsed)
                .reason(viable ? "Viable within organ safety window" : "Exceeded organ viability window")
                .selectedMode(bestMode)
                .routeOptions(routeOptions)
                .build();
    }

    private boolean isModeEligible(String mode, ViabilityRequestDTO request) {
        if (mode.equals("ROAD")) {
            return true;
        }
        if (mode.equals("AIR")) {
            double airMinDistance = systemSettingService.getDoubleValue("transport.air.min_distance_km", 50d);
            double distance = haversine(request.getDonorLatitude(), request.getDonorLongitude(), request.getRecipientLatitude(), request.getRecipientLongitude());
            return distance >= airMinDistance;
        }
        if (mode.equals("TRAIN")) {
            return request.getDonorLatitude() != null && request.getDonorLongitude() != null && request.getRecipientLatitude() != null && request.getRecipientLongitude() != null;
        }
        return false;
    }

    private double getSafeHours(String organType) {
        String key = "shelflife." + organType + ".hours";
        return switch (organType) {
            case "HEART" -> systemSettingService.getDoubleValue(key, 4d);
            case "LUNG" -> systemSettingService.getDoubleValue(key, 6d);
            case "LIVER" -> systemSettingService.getDoubleValue(key, 12d);
            case "KIDNEY" -> systemSettingService.getDoubleValue(key, 24d);
            default -> systemSettingService.getDoubleValue(key, 6d);
        };
    }

    private double getTrafficPenalty(String trafficLevel) {
        String level = trafficLevel == null ? "LOW" : trafficLevel.trim().toUpperCase();
        return switch (level) {
            case "LOW" -> systemSettingService.getDoubleValue("transport.traffic.LOW", 0d);
            case "MODERATE" -> systemSettingService.getDoubleValue("transport.traffic.MODERATE", 15d);
            case "SEVERE" -> systemSettingService.getDoubleValue("transport.traffic.SEVERE", 40d);
            default -> 0d;
        };
    }

    private String normalizeOrgan(String organType) {
        return organType == null ? "LIVER" : organType.trim().toUpperCase();
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }
}
