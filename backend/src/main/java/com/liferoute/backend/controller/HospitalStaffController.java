package com.liferoute.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.liferoute.backend.dto.request.CreateHospitalStaffRequestDTO;
import com.liferoute.backend.dto.response.UserProfileResponseDTO;
import com.liferoute.backend.security.CustomUserDetails;
import com.liferoute.backend.service.HospitalStaffService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hospital/staff")
@RequiredArgsConstructor
public class HospitalStaffController {

	private final HospitalStaffService hospitalStaffService;
	
	@PostMapping
    public ResponseEntity<UserProfileResponseDTO> createStaff(
            @Valid @RequestBody CreateHospitalStaffRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            HttpServletRequest servletRequest) {
        return ResponseEntity.ok(
                hospitalStaffService.createStaffMember(request, currentUser, servletRequest.getRemoteAddr())
        );
    }
	
	@GetMapping
    public ResponseEntity<List<UserProfileResponseDTO>> listStaff(
            @RequestParam(required = false) Long hospitalId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(hospitalStaffService.getHospitalStaff(hospitalId, currentUser));
    }
}
