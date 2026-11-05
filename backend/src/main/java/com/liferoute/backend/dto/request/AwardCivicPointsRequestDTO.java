package com.liferoute.backend.dto.request;

import com.liferoute.domain.enums.CivicActivityType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AwardCivicPointsRequestDTO {

	@NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Activity type is required")
    private CivicActivityType activityType;

    @Positive(message = "Custom points override must be positive")
    private Integer customPoints;

    @Size(max = 255, message = "Remarks cannot exceed 255 characters")
    private String remarks;
}
