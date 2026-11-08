package com.liferoute.backend.logistics.service.impl;

import com.liferoute.backend.logistics.service.MapsClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class MockMapsClient implements MapsClient {
    @Override
    public RouteEstimate estimateRoute(double fromLat, double fromLng, double toLat, double toLng, String mode) {
        double distanceKm = haversine(fromLat, fromLng, toLat, toLng);
        double speedKmph = switch (mode.toUpperCase()) {
            case "ROAD" -> 32.0;
            case "AIR" -> 320.0;
            case "TRAIN" -> 48.0;
            default -> 35.0;
        };

        int durationMinutes = (int) Math.max(12, Math.round((distanceKm / speedKmph) * 60));
        return new RouteEstimate(Math.round(distanceKm * 10.0) / 10.0, durationMinutes, mode + " route estimate");
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
