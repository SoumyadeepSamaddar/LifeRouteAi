package com.liferoute.backend.dto.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityCodeResponseDTO {

	private Long id;
    private Long hospitalId;
    private String hospitalName;
    private String rawCode;
    private String codeMasked;
    private Instant expiresAt;
}
