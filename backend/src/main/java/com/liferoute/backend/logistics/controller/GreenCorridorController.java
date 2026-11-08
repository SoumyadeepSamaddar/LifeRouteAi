package com.liferoute.backend.logistics.controller;

import com.liferoute.backend.logistics.dto.GreenCorridorRequestDTO;
import com.liferoute.backend.logistics.entity.GreenCorridor;
import com.liferoute.backend.logistics.entity.Route;
import com.liferoute.backend.logistics.repository.GreenCorridorRepository;
import com.liferoute.backend.logistics.repository.RouteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/transport")
@RequiredArgsConstructor
public class GreenCorridorController {
    private final GreenCorridorRepository greenCorridorRepository;
    private final RouteRepository routeRepository;

    @PostMapping("/green-corridor")
    public ResponseEntity<GreenCorridor> create(@Valid @RequestBody GreenCorridorRequestDTO request) {
        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException("Route not found: " + request.getRouteId()));

        GreenCorridor corridor = GreenCorridor.builder()
                .route(route)
                .status("REQUESTED")
                .policeContact(request.getPoliceContact())
                .referenceNote(request.getReferenceNote())
                .requestedBy(request.getRequestedBy())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return ResponseEntity.ok(greenCorridorRepository.save(corridor));
    }

    @PatchMapping("/green-corridor/{id}/status")
    public ResponseEntity<GreenCorridor> updateStatus(@PathVariable Long id, @RequestParam String status) {
        GreenCorridor corridor = greenCorridorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Green corridor not found: " + id));
        corridor.setStatus(status);
        corridor.setUpdatedAt(Instant.now());
        return ResponseEntity.ok(greenCorridorRepository.save(corridor));
    }
}
