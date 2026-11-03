package com.liferoute.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyCodeRequestDTO {
	@NotBlank(message = "Security code is required")
    private String code;
}
