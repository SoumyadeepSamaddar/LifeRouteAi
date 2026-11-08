package com.liferoute.backend.logistics.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulationRequestDTO {
    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double donorLatitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double donorLongitude;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double recipientLatitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double recipientLongitude;

    @Builder.Default
    private Double averageSpeedKmph = 42.0;

    @Builder.Default
    private Integer pointCount = 12;
}
