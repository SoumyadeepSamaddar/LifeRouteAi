package com.liferoute.backend.dto.response;

import java.time.Instant;

import com.liferoute.domain.enums.CivicActivityType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CivicPointResponseDTO {

	private Long id;
    private Long userId;
    private CivicActivityType activityType;
    private Integer points;
    private String remarks;
    private Instant awardedAt;
}
