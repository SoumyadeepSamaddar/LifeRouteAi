package com.liferoute.backend.logistics.service.impl;

import com.liferoute.backend.logistics.entity.HazardFlag;
import com.liferoute.backend.logistics.repository.HazardFlagRepository;
import com.liferoute.backend.logistics.service.HazardFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HazardFlagServiceImpl implements HazardFlagService {
    private final HazardFlagRepository hazardFlagRepository;

    @Override
    public List<HazardFlag> findAll() {
        return hazardFlagRepository.findAll();
    }

    @Override
    public HazardFlag create(HazardFlag hazardFlag) {
        if (hazardFlag.getCreatedAt() == null) {
            hazardFlag.setCreatedAt(Instant.now());
        }
        if (hazardFlag.getUpdatedAt() == null) {
            hazardFlag.setUpdatedAt(Instant.now());
        }
        return hazardFlagRepository.save(hazardFlag);
    }

    @Override
    public HazardFlag updateStatus(Long id, String status) {
        HazardFlag hazard = hazardFlagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hazard not found: " + id));
        hazard.setStatus(status);
        hazard.setUpdatedAt(Instant.now());
        return hazardFlagRepository.save(hazard);
    }
}
