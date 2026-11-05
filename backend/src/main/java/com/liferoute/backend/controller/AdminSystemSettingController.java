package com.liferoute.backend.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.liferoute.backend.dto.request.UpdateSystemSettingRequestDTO;
import com.liferoute.backend.dto.response.SystemSettingResponseDTO;
import com.liferoute.backend.entity.AuditLog;
import com.liferoute.backend.repository.AuditLogRepository;
import com.liferoute.backend.security.CustomUserDetails;
import com.liferoute.backend.service.SystemSettingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminSystemSettingController {

	private final SystemSettingService systemSettingService;
    private final AuditLogRepository auditLogRepository;
    
    @GetMapping("/settings")
    public ResponseEntity<List<SystemSettingResponseDTO>> getAllSettings(
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(systemSettingService.getAllSettings(category));
    }
    
    @GetMapping("/settings/{key}")
    public ResponseEntity<SystemSettingResponseDTO> getSettingByKey(@PathVariable String key) {
        return ResponseEntity.ok(systemSettingService.getSettingByKey(key));
    }
    
    @PutMapping("/settings/{key}")
    public ResponseEntity<SystemSettingResponseDTO> updateSetting(
            @PathVariable String key,
            @Valid @RequestBody UpdateSystemSettingRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            HttpServletRequest servletRequest) {
        return ResponseEntity.ok(
                systemSettingService.updateSetting(key, request, currentUser.getId(), servletRequest.getRemoteAddr())
        );
    }
    
    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(
                auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
        );
    }
    
}
