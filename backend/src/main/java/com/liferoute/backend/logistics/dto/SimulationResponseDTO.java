package com.liferoute.backend.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulationResponseDTO {
    private Double totalDistanceKm;
    private Double etaMinutes;
    private List<RoutePointDTO> points;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoutePointDTO {
        private Double latitude;
        private Double longitude;
        private Integer segmentIndex;
        private Double distanceFromOriginKm;
    }
}
