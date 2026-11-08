package com.liferoute.backend.logistics.controller;

import com.liferoute.backend.logistics.dto.SimulationRequestDTO;
import com.liferoute.backend.logistics.dto.SimulationResponseDTO;
import com.liferoute.backend.logistics.dto.TransportRequestDTO;
import com.liferoute.backend.logistics.dto.TransportResponseDTO;
import com.liferoute.backend.logistics.dto.ViabilityRequestDTO;
import com.liferoute.backend.logistics.dto.ViabilityResponseDTO;
import com.liferoute.backend.logistics.service.SimulationService;
import com.liferoute.backend.logistics.service.TransportService;
import com.liferoute.backend.logistics.service.ViabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transport")
@RequiredArgsConstructor
public class TransportController {
    private final ViabilityService viabilityService;
    private final TransportService transportService;
    private final SimulationService simulationService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "OK", "module", "transport"));
    }

    @PostMapping("/viability")
    public ResponseEntity<ViabilityResponseDTO> evaluateViability(@Valid @RequestBody ViabilityRequestDTO request) {
        return ResponseEntity.ok(viabilityService.evaluateCandidate(request));
    }

    @PostMapping("/options")
    public ResponseEntity<TransportResponseDTO> buildRouteOptions(@Valid @RequestBody TransportRequestDTO request) {
        return ResponseEntity.ok(transportService.buildRouteOptions(request));
    }

    @PostMapping({"/simulate", "/simulation"})
    public ResponseEntity<SimulationResponseDTO> simulateRoute(@Valid @RequestBody SimulationRequestDTO request) {
        return ResponseEntity.ok(simulationService.simulateRoute(request));
    }
}
