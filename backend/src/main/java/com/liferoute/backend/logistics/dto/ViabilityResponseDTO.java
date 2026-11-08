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
public class ViabilityResponseDTO {
    private Boolean viable;
    private Double totalTimeMinutes;
    private Double remainingBufferMinutes;
    private Double safeHours;
    private Double elapsedSinceRetrievalMinutes;
    private String reason;
    private String selectedMode;
    private List<RouteOptionDTO> routeOptions;
}
