package com.liferoute.backend.logistics.service;

import com.liferoute.backend.logistics.entity.Alert;

import java.util.List;

public interface AlertService {
    List<Alert> findAll();
    Alert create(Alert alert);
    Alert markRead(Long id);
}
