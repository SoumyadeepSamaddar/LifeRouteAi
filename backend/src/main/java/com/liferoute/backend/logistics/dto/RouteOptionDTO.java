package com.liferoute.backend.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteOptionDTO {
    private String mode;
    private Double distanceKm;
    private Double etaMinutes;
    private Double totalTimeMinutes;
    private Double remainingBufferMinutes;
    private Boolean viable;
    private String reason;
    private Boolean isSelected;
}
