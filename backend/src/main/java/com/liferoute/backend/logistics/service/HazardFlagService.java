package com.liferoute.backend.logistics.service;

import com.liferoute.backend.logistics.entity.HazardFlag;

import java.util.List;

public interface HazardFlagService {
    List<HazardFlag> findAll();
    HazardFlag create(HazardFlag hazardFlag);
    HazardFlag updateStatus(Long id, String status);
}
