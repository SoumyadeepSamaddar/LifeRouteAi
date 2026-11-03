package com.liferoute.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liferoute.backend.dto.request.ActivateHospitalRequestDTO;
import com.liferoute.backend.dto.request.VerifyCodeRequestDTO;
import com.liferoute.backend.dto.response.ApiMessageResponseDTO;
import com.liferoute.backend.dto.response.VerifyCodeResponseDTO;
import com.liferoute.backend.service.SecurityCodeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hospital/activate")
@RequiredArgsConstructor
public class HospitalActivationController {

	private final SecurityCodeService securityCodeService;
	
	@PostMapping("/verify")
    public ResponseEntity<VerifyCodeResponseDTO> verifySecurityCode(
            @Valid @RequestBody VerifyCodeRequestDTO request) {
        return ResponseEntity.ok(securityCodeService.verifyCode(request.getCode()));
    }

    @PostMapping
    public ResponseEntity<ApiMessageResponseDTO> activateHospital(
            @Valid @RequestBody ActivateHospitalRequestDTO request,
            HttpServletRequest httpRequest) {
        securityCodeService.activateHospital(request, httpRequest.getRemoteAddr());
        return ResponseEntity.ok(new ApiMessageResponseDTO("Hospital activated successfully. You may now sign in."));
    }
}


