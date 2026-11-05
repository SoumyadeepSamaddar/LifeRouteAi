package com.liferoute.backend.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CivicSummaryResponseDTO {

	private Long userId;
    private String userName;
    private String userEmail;
    private Integer totalPoints;
    private String civicTier;
    private Double priorityBonusMultiplier;
    private List<CivicPointResponseDTO> history;
}
