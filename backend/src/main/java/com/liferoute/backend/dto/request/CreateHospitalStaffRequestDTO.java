package com.liferoute.backend.dto.request;

import com.liferoute.domain.enums.StaffRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateHospitalStaffRequestDTO {

	@NotBlank(message = "Staff name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Provide a valid email address")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Temporary password is required")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "Password must be at least 8 characters and include uppercase, lowercase, and a number"
    )
    private String password;

    @NotNull(message = "Staff role is required (COORDINATOR, DOCTOR, DISPATCH)")
    private StaffRole staffRole;

    private Long hospitalId; // Used when an ADMIN creates staff for a specific hospital
}
