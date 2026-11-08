package com.liferoute.backend.logistics.service.impl;

import com.liferoute.backend.logistics.dto.RouteOptionDTO;
import com.liferoute.backend.logistics.dto.TransportRequestDTO;
import com.liferoute.backend.logistics.dto.TransportResponseDTO;
import com.liferoute.backend.logistics.service.MapsClient;
import com.liferoute.backend.logistics.service.TransportService;
import com.liferoute.backend.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {
    private final MapsClient mapsClient;
    private final SystemSettingService systemSettingService;

    @Override
    public TransportResponseDTO buildRouteOptions(TransportRequestDTO request) {
        String organType = request.getOrganType() == null ? "LIVER" : request.getOrganType().trim().toUpperCase();
        double handoverBufferMinutes = systemSettingService.getDoubleValue("shelflife.handover_buffer_mins", 30d);
        double trafficPenalty = getTrafficPenalty(request.getTrafficLevel());
        double elapsed = request.getElapsedSinceRetrievalMinutes() == null ? 0d : request.getElapsedSinceRetrievalMinutes();

        List<RouteOptionDTO> options = new ArrayList<>();
        List<String> modes = new ArrayList<>();
        modes.add("ROAD");
        if (shouldAllowAir(request)) {
            modes.add("AIR");
        }
        if (Boolean.TRUE.equals(request.getTrainAvailable())) {
            modes.add("TRAIN");
        }

        for (String mode : modes) {
            MapsClient.RouteEstimate estimate = mapsClient.estimateRoute(
                    request.getDonorLatitude(), request.getDonorLongitude(),
                    request.getRecipientLatitude(), request.getRecipientLongitude(),
                    mode
            );

            double totalTimeMinutes = estimate.durationMinutes() + trafficPenalty + handoverBufferMinutes;
            double safeHours = getSafeHours(organType);
            double buffer = (safeHours * 60) - (elapsed + totalTimeMinutes);

            options.add(RouteOptionDTO.builder()
                    .mode(mode)
                    .distanceKm(Math.round(estimate.distanceKm() * 10.0) / 10.0)
                    .etaMinutes((double) estimate.durationMinutes())
                    .totalTimeMinutes(totalTimeMinutes)
                    .remainingBufferMinutes(buffer)
                    .viable(elapsed + totalTimeMinutes <= safeHours * 60)
                    .reason(elapsed + totalTimeMinutes <= safeHours * 60 ? "Viable within safety window" : "Exceeds safe time window")
                    .isSelected(false)
                    .build());
        }

        if (options.isEmpty()) {
            options.add(RouteOptionDTO.builder()
                    .mode("ROAD")
                    .distanceKm(0d)
                    .etaMinutes(0d)
                    .totalTimeMinutes(Double.MAX_VALUE)
                    .remainingBufferMinutes(-1d)
                    .viable(false)
                    .reason("No mode available")
                    .isSelected(true)
                    .build());
        }

        options.sort(Comparator.comparing(RouteOptionDTO::getTotalTimeMinutes));
        String selectedMode = options.get(0).getMode();
        String backupMode = options.size() > 1 ? options.get(1).getMode() : null;
        options.get(0).setIsSelected(true);

        double totalDistanceKm = options.stream().mapToDouble(RouteOptionDTO::getDistanceKm).sum();
        double etaMinutes = options.stream().mapToDouble(RouteOptionDTO::getEtaMinutes).sum();

        return TransportResponseDTO.builder()
                .selectedMode(selectedMode)
                .backupMode(backupMode)
                .options(options)
                .totalDistanceKm(Math.round(totalDistanceKm * 10.0) / 10.0)
                .etaMinutes(Math.round(etaMinutes * 10.0) / 10.0)
                .summary("Selected " + selectedMode + " transport route using the lowest viable total time")
                .build();
    }

    private boolean shouldAllowAir(TransportRequestDTO request) {
        double minimumDistance = systemSettingService.getDoubleValue("transport.air.min_distance_km", 50d);
        double distanceKm = haversine(
                request.getDonorLatitude(), request.getDonorLongitude(),
                request.getRecipientLatitude(), request.getRecipientLongitude()
        );
        return distanceKm >= minimumDistance && (Boolean.TRUE.equals(request.getHasAirport()) || Boolean.TRUE.equals(request.getHasHelipad()));
    }

    private double getSafeHours(String organType) {
        return switch (organType) {
            case "HEART" -> systemSettingService.getDoubleValue("shelflife.HEART.hours", 4d);
            case "LUNG" -> systemSettingService.getDoubleValue("shelflife.LUNG.hours", 6d);
            case "LIVER" -> systemSettingService.getDoubleValue("shelflife.LIVER.hours", 12d);
            case "KIDNEY" -> systemSettingService.getDoubleValue("shelflife.KIDNEY.hours", 24d);
            default -> systemSettingService.getDoubleValue("shelflife.LIVER.hours", 12d);
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
