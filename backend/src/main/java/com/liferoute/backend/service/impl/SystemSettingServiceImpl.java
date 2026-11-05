package com.liferoute.backend.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liferoute.backend.dto.request.UpdateSystemSettingRequestDTO;
import com.liferoute.backend.dto.response.SystemSettingResponseDTO;
import com.liferoute.backend.entity.AuditLog;
import com.liferoute.backend.entity.SystemSetting;
import com.liferoute.backend.entity.User;
import com.liferoute.backend.repository.AuditLogRepository;
import com.liferoute.backend.repository.SystemSettingRepository;
import com.liferoute.backend.repository.UserRepository;
import com.liferoute.backend.service.SystemSettingService;
import com.liferoute.domain.enums.AuditActionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl implements SystemSettingService {
	
	private final SystemSettingRepository systemSettingRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<SystemSettingResponseDTO> getAllSettings(String category) {
        List<SystemSetting> settings = (category != null && !category.isBlank())
                ? systemSettingRepository.findByCategoryOrderBySettingKeyAsc(category.trim().toUpperCase())
                : systemSettingRepository.findAll();

        return settings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public SystemSettingResponseDTO getSettingByKey(String key) {
        SystemSetting setting = systemSettingRepository.findById(key)
                .orElseThrow(() -> new IllegalArgumentException("System setting not found: " + key));
        return mapToDTO(setting);
    }
    
    @Override
    @Transactional
    public SystemSettingResponseDTO updateSetting(String key, UpdateSystemSettingRequestDTO request, Long adminUserId, String ipAddress) {
        SystemSetting setting = systemSettingRepository.findById(key)
                .orElseThrow(() -> new IllegalArgumentException("System setting not found: " + key));

        validateNumericSetting(key, request.getSettingValue().trim());
        
        String oldValue = setting.getSettingValue();
        setting.setSettingValue(request.getSettingValue().trim());
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            setting.setDescription(request.getDescription().trim());
        }
        setting.setUpdatedBy(adminUserId);
        setting.setUpdatedAt(Instant.now());

        SystemSetting saved = systemSettingRepository.save(setting);
        
        User admin = userRepository.findById(adminUserId).orElse(null);
        AuditLog log = AuditLog.builder()
                .userId(adminUserId)
                .userEmail(admin != null ? admin.getEmail() : "admin@liferoute.ai")
                .userRole("ADMIN")
                .actionType(AuditActionType.SETTINGS)
                .targetObject("SystemSetting:" + key)
                .details("Changed value from '" + oldValue + "' to '" + saved.getSettingValue() + "'")
                .ipAddress(ipAddress)
                .createdAt(Instant.now())
                .build();
        auditLogRepository.save(log);

        return mapToDTO(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public double getDoubleValue(String key, double defaultValue) {
        return systemSettingRepository.findById(key)
                .map(s -> {
                    try {
                        return Double.parseDouble(s.getSettingValue());
                    } catch (NumberFormatException e) {
                        return defaultValue;
                    }
                })
                .orElse(defaultValue);
    }
    
    @Override
    @Transactional(readOnly = true)
    public int getIntValue(String key, int defaultValue) {
        return systemSettingRepository.findById(key)
                .map(s -> {
                    try {
                        return Integer.parseInt(s.getSettingValue());
                    } catch (NumberFormatException e) {
                        return defaultValue;
                    }
                })
                .orElse(defaultValue);
    }
    
    private void validateNumericSetting(String key, String value) {
        try {
            double parsed = Double.parseDouble(value);
            if (parsed < 0) {
                throw new IllegalArgumentException("Setting value for " + key + " cannot be negative.");
            }
            if (key.startsWith("weight.") && parsed > 1.0) {
                throw new IllegalArgumentException("Algorithm weight " + key + " must be between 0.00 and 1.00.");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Setting value for " + key + " must be a valid number.");
        }
    }

    private SystemSettingResponseDTO mapToDTO(SystemSetting s) {
        return SystemSettingResponseDTO.builder()
                .settingKey(s.getSettingKey())
                .settingValue(s.getSettingValue())
                .category(s.getCategory())
                .description(s.getDescription())
                .updatedBy(s.getUpdatedBy())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
    
}
