package com.liferoute.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.liferoute.backend.dto.response.SecurityCodeResponseDTO;
import com.liferoute.backend.entity.Hospital;
import com.liferoute.backend.entity.SecurityCode;
import com.liferoute.backend.security.CustomUserDetails;
import com.liferoute.backend.service.HospitalService;
import com.liferoute.backend.service.SecurityCodeService;
import com.liferoute.domain.enums.HospitalStatus;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminHospitalController {

	private final HospitalService hospitalService;
    private final SecurityCodeService securityCodeService;
    
    @GetMapping("/hospitals")
    public ResponseEntity<List<Hospital>> listHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }
    
    @GetMapping("/hospitals/{id}")
    public ResponseEntity<Hospital> getHospital(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.getHospitalById(id));
    }
    
    @PostMapping("/hospitals")
    public ResponseEntity<Hospital> createHospital(
            @RequestBody Hospital hospital,
            @AuthenticationPrincipal CustomUserDetails admin,
            HttpServletRequest request) {
        return ResponseEntity.ok(hospitalService.createHospital(hospital, admin, request.getRemoteAddr()));
    }
    
    @PatchMapping("/hospitals/{id}/status")
    public ResponseEntity<Hospital> updateHospitalStatus(
            @PathVariable Long id,
            @RequestParam HospitalStatus status,
            @AuthenticationPrincipal CustomUserDetails admin,
            HttpServletRequest request) {
        return ResponseEntity.ok(hospitalService.updateHospitalStatus(id, status, admin, request.getRemoteAddr()));
    }
    
    @GetMapping("/security-codes")
    public ResponseEntity<List<SecurityCode>> listSecurityCodes() {
        return ResponseEntity.ok(securityCodeService.getAllSecurityCodes());
    }
    
    @PostMapping("/security-codes")
    public ResponseEntity<SecurityCodeResponseDTO> generateSecurityCode(
            @RequestParam Long hospitalId,
            @RequestParam(defaultValue = "72") int validHours,
            @AuthenticationPrincipal CustomUserDetails admin,
            HttpServletRequest request) {
        return ResponseEntity.ok(securityCodeService.generateCode(hospitalId, validHours, admin, request.getRemoteAddr()));
    }
    
    @PostMapping("/security-codes/{id}/revoke")
    public ResponseEntity<SecurityCode> revokeSecurityCode(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails admin,
            HttpServletRequest request) {
        return ResponseEntity.ok(securityCodeService.revokeCode(id, admin, request.getRemoteAddr()));
    }
}
