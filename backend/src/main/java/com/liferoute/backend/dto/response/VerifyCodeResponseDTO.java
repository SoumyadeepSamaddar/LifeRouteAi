package com.liferoute.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyCodeResponseDTO {

	private boolean valid;
    private Long hospitalId;
    private String hospitalName;
    private String city;
}
