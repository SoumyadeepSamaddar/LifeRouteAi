package com.liferoute.backend.logistics.controller;

import com.liferoute.backend.logistics.entity.HazardFlag;
import com.liferoute.backend.logistics.service.HazardFlagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
@RequiredArgsConstructor
public class HazardFlagController {
    private final HazardFlagService hazardFlagService;

    @GetMapping("/hazards")
    public ResponseEntity<List<HazardFlag>> findAll() {
        return ResponseEntity.ok(hazardFlagService.findAll());
    }

    @PostMapping("/hazards")
    public ResponseEntity<HazardFlag> create(@Valid @RequestBody HazardFlag hazardFlag) {
        return ResponseEntity.ok(hazardFlagService.create(hazardFlag));
    }

    @PatchMapping("/hazards/{id}/status")
    public ResponseEntity<HazardFlag> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(hazardFlagService.updateStatus(id, status));
    }
}
