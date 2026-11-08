package com.liferoute.backend.logistics.service;

import com.liferoute.backend.logistics.dto.SimulationRequestDTO;
import com.liferoute.backend.logistics.dto.SimulationResponseDTO;

public interface SimulationService {
    SimulationResponseDTO simulateRoute(SimulationRequestDTO request);
}
