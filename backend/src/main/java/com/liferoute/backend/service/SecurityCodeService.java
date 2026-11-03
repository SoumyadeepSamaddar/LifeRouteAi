package com.liferoute.backend.service;

import java.util.List;

import com.liferoute.backend.dto.request.ActivateHospitalRequestDTO;
import com.liferoute.backend.dto.response.SecurityCodeResponseDTO;
import com.liferoute.backend.dto.response.VerifyCodeResponseDTO;
import com.liferoute.backend.entity.SecurityCode;
import com.liferoute.backend.security.CustomUserDetails;

public interface SecurityCodeService {

	List<SecurityCode> getAllSecurityCodes();
    SecurityCodeResponseDTO generateCode(Long hospitalId, int validHours, CustomUserDetails admin, String ipAddress);
    SecurityCode revokeCode(Long codeId, CustomUserDetails admin, String ipAddress);
    VerifyCodeResponseDTO verifyCode(String rawCode);
    void activateHospital(ActivateHospitalRequestDTO request, String ipAddress);
}
