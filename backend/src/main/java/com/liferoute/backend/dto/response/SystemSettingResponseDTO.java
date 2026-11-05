package com.liferoute.backend.dto.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemSettingResponseDTO {

	private String settingKey;
    private String settingValue;
    private String category;
    private String description;
    private Long updatedBy;
    private Instant updatedAt;
}
