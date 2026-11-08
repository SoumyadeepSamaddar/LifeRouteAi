package com.liferoute.backend.logistics.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViabilityRequestDTO {
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

    @NotBlank
    private String organType;

    @NotNull
    @DecimalMin("0.0")
    private Double elapsedSinceRetrievalMinutes;

    private String trafficLevel = "LOW";
    private Integer hazardPenaltyMinutes = 0;
}
