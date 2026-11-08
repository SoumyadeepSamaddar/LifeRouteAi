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
public class TransportResponseDTO {
    private String selectedMode;
    private String backupMode;
    private List<RouteOptionDTO> options;
    private Double totalDistanceKm;
    private Double etaMinutes;
    private String summary;
}
