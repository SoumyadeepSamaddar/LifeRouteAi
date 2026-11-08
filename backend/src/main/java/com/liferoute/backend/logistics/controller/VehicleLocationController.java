package com.liferoute.backend.logistics.controller;

import com.liferoute.backend.logistics.entity.VehicleLocation;
import com.liferoute.backend.logistics.repository.VehicleLocationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
@RequiredArgsConstructor
public class VehicleLocationController {
    private final VehicleLocationRepository vehicleLocationRepository;

    @GetMapping("/vehicle-locations")
    public ResponseEntity<List<VehicleLocation>> list() {
        return ResponseEntity.ok(vehicleLocationRepository.findAll());
    }

    @PostMapping("/vehicle-locations")
    public ResponseEntity<VehicleLocation> create(@Valid @RequestBody VehicleLocation vehicleLocation) {
        return ResponseEntity.ok(vehicleLocationRepository.save(vehicleLocation));
    }
}
