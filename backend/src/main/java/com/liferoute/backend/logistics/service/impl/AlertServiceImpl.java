package com.liferoute.backend.logistics.service.impl;

import com.liferoute.backend.logistics.entity.Alert;
import com.liferoute.backend.logistics.repository.AlertRepository;
import com.liferoute.backend.logistics.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertRepository alertRepository;

    @Override
    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    @Override
    public Alert create(Alert alert) {
        if (alert.getCreatedAt() == null) {
            alert.setCreatedAt(Instant.now());
        }
        return alertRepository.save(alert);
    }

    @Override
    public Alert markRead(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + id));
        alert.setReadAt(Instant.now());
        return alertRepository.save(alert);
    }
}
