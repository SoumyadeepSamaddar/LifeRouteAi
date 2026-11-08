package com.liferoute.backend.logistics.service;

import com.liferoute.backend.logistics.dto.TransportRequestDTO;
import com.liferoute.backend.logistics.dto.TransportResponseDTO;

public interface TransportService {
    TransportResponseDTO buildRouteOptions(TransportRequestDTO request);
}
