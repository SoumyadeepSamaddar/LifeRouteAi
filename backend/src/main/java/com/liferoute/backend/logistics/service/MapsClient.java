package com.liferoute.backend.logistics.service;

public interface MapsClient {
    RouteEstimate estimateRoute(double fromLat, double fromLng, double toLat, double toLng, String mode);

    record RouteEstimate(double distanceKm, int durationMinutes, String summary) {
    }
}
