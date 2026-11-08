package com.liferoute.backend.logistics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GreenCorridorRequestDTO {
    @NotNull
    private Long routeId;

    @NotBlank
    private String policeContact;

    private String referenceNote;
    private Long requestedBy;
}
