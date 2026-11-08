package com.liferoute.backend.logistics.controller;

import com.liferoute.backend.logistics.entity.Alert;
import com.liferoute.backend.logistics.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @GetMapping("/alerts")
    public ResponseEntity<List<Alert>> findAll() {
        return ResponseEntity.ok(alertService.findAll());
    }

    @PostMapping("/alerts")
    public ResponseEntity<Alert> create(@RequestBody Alert alert) {
        return ResponseEntity.ok(alertService.create(alert));
    }

    @PatchMapping("/alerts/{id}/read")
    public ResponseEntity<Alert> markRead(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.markRead(id));
    }
}
