package com.liferoute.backend.logistics.service;

import com.liferoute.backend.logistics.dto.ViabilityRequestDTO;
import com.liferoute.backend.logistics.dto.ViabilityResponseDTO;

public interface ViabilityService {
    ViabilityResponseDTO evaluateCandidate(ViabilityRequestDTO request);
}
