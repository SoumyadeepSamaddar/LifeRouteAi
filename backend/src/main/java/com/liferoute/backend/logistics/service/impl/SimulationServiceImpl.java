package com.liferoute.backend.logistics.service.impl;

import com.liferoute.backend.logistics.dto.SimulationRequestDTO;
import com.liferoute.backend.logistics.dto.SimulationResponseDTO;
import com.liferoute.backend.logistics.service.SimulationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationServiceImpl implements SimulationService {
    @Override
    public SimulationResponseDTO simulateRoute(SimulationRequestDTO request) {
        double startLat = request.getDonorLatitude();
        double startLng = request.getDonorLongitude();
        double endLat = request.getRecipientLatitude();
        double endLng = request.getRecipientLongitude();
        int pointCount = Math.max(4, request.getPointCount() == null ? 12 : request.getPointCount());
        double averageSpeed = request.getAverageSpeedKmph() == null ? 42.0 : request.getAverageSpeedKmph();

        double totalDistanceKm = haversine(startLat, startLng, endLat, endLng);
        double etaMinutes = (totalDistanceKm / Math.max(averageSpeed, 1.0)) * 60.0;

        List<SimulationResponseDTO.RoutePointDTO> points = new ArrayList<>();
        for (int idx = 0; idx < pointCount; idx++) {
            double fraction = pointCount == 1 ? 0 : (double) idx / (pointCount - 1);
            double lat = startLat + (endLat - startLat) * fraction;
            double lng = startLng + (endLng - startLng) * fraction;
            double cumulativeDistance = totalDistanceKm * fraction;

            points.add(SimulationResponseDTO.RoutePointDTO.builder()
                    .latitude(Math.round(lat * 100000.0) / 100000.0)
                    .longitude(Math.round(lng * 100000.0) / 100000.0)
                    .segmentIndex(idx)
                    .distanceFromOriginKm(Math.round(cumulativeDistance * 10.0) / 10.0)
                    .build());
        }

        return SimulationResponseDTO.builder()
                .totalDistanceKm(Math.round(totalDistanceKm * 10.0) / 10.0)
                .etaMinutes(Math.round(etaMinutes * 10.0) / 10.0)
                .points(points)
                .build();
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
