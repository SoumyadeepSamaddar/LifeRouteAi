package com.liferoute.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateSystemSettingRequestDTO {

	@NotBlank(message = "Setting value cannot be blank")
    @Size(max = 500, message = "Setting value cannot exceed 500 characters")
    private String settingValue;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
