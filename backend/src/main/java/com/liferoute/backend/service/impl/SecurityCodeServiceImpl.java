package com.liferoute.backend.service.impl;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liferoute.backend.dto.request.ActivateHospitalRequestDTO;
import com.liferoute.backend.dto.response.SecurityCodeResponseDTO;
import com.liferoute.backend.dto.response.VerifyCodeResponseDTO;
import com.liferoute.backend.entity.Hospital;
import com.liferoute.backend.entity.SecurityCode;
import com.liferoute.backend.entity.User;
import com.liferoute.backend.repository.HospitalRepository;
import com.liferoute.backend.repository.SecurityCodeRepository;
import com.liferoute.backend.repository.UserRepository;
import com.liferoute.backend.security.CustomUserDetails;
import com.liferoute.backend.service.AuditService;
import com.liferoute.backend.service.SecurityCodeService;
import com.liferoute.domain.enums.AuditActionType;
import com.liferoute.domain.enums.HospitalStatus;
import com.liferoute.domain.enums.Role;
import com.liferoute.domain.enums.SecurityCodeStatus;
import com.liferoute.domain.enums.StaffRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityCodeServiceImpl implements SecurityCodeService {

	private static final String ALPHANUM = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private final SecureRandom secureRandom = new SecureRandom();

    private final SecurityCodeRepository securityCodeRepository;
    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;
    
    @Override
    @Transactional(readOnly = true)
    public List<SecurityCode> getAllSecurityCodes() {
        return securityCodeRepository.findAll();
    }
    
    @Override
    @Transactional
    public SecurityCodeResponseDTO generateCode(Long hospitalId, int validHours, CustomUserDetails admin, String ipAddress) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));
        
        String part1 = randomBlock(4);
        String part2 = randomBlock(4);
        String rawCode = "LR-" + part1 + "-" + part2;
        String masked = "LR-****-" + part2;
        
        SecurityCode code = SecurityCode.builder()
                .hospitalId(hospitalId)
                .codeMasked(masked)
                .codeHash(passwordEncoder.encode(rawCode))
                .status(SecurityCodeStatus.ACTIVE)
                .createdBy(admin.getId())
                .expiresAt(Instant.now().plus(validHours > 0 ? validHours : 72, ChronoUnit.HOURS))
                .build();
        
        SecurityCode saved = securityCodeRepository.save(code);
        auditService.log(admin, AuditActionType.SECURITY_CODE, "Hospital:" + hospitalId,
                "Generated security code " + masked, ipAddress);
        
        return SecurityCodeResponseDTO.builder()
                .id(saved.getId())
                .hospitalId(hospital.getId())
                .hospitalName(hospital.getName())
                .rawCode(rawCode)
                .codeMasked(masked)
                .expiresAt(saved.getExpiresAt())
                .build();
    }
    
    @Override
    @Transactional
    public SecurityCode revokeCode(Long codeId, CustomUserDetails admin, String ipAddress) {
        SecurityCode code = securityCodeRepository.findById(codeId)
                .orElseThrow(() -> new IllegalArgumentException("Security code not found"));
        code.setStatus(SecurityCodeStatus.REVOKED);
        SecurityCode saved = securityCodeRepository.save(code);

        auditService.log(admin, AuditActionType.SECURITY_CODE, "SecurityCode:" + codeId,
                "Revoked security code " + code.getCodeMasked(), ipAddress);
        return saved;
    }
    
    @Override
    @Transactional(readOnly = true)
    public VerifyCodeResponseDTO verifyCode(String rawCode) {
        SecurityCode matched = findMatchingActiveCode(rawCode.trim().toUpperCase());
        Hospital hospital = hospitalRepository.findById(matched.getHospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        return VerifyCodeResponseDTO.builder()
                .valid(true)
                .hospitalId(hospital.getId())
                .hospitalName(hospital.getName())
                .city(hospital.getCity())
                .build();
    }
    
    @Override
    @Transactional
    public void activateHospital(ActivateHospitalRequestDTO request, String ipAddress) {
        SecurityCode matched = findMatchingActiveCode(request.getCode().trim().toUpperCase());

        if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        Hospital hospital = hospitalRepository.findById(matched.getHospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        hospital.setStatus(HospitalStatus.ACTIVE);
        hospitalRepository.save(hospital);

        matched.setStatus(SecurityCodeStatus.USED);
        matched.setUsedAt(Instant.now());
        securityCodeRepository.save(matched);

        User hospitalAdmin = User.builder()
                .name(request.getFullName())
                .email(request.getEmail().toLowerCase())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.HOSPITAL)
                .staffRole(StaffRole.HOSPITAL_ADMIN)
                .hospitalId(hospital.getId())
                .active(true)
                .build();

        userRepository.save(hospitalAdmin);
        auditService.log(null, AuditActionType.SECURITY_CODE, "Hospital:" + hospital.getId(),
                "Hospital activated by " + request.getEmail(), ipAddress);
    }
    
    private SecurityCode findMatchingActiveCode(String normalizedCode) {
        List<SecurityCode> allCodes = securityCodeRepository.findAll();
        for (SecurityCode sc : allCodes) {
            if (passwordEncoder.matches(normalizedCode, sc.getCodeHash())) {
                if (sc.getStatus() == SecurityCodeStatus.USED) {
                    throw new IllegalArgumentException("This security code has already been used.");
                }
                if (sc.getStatus() == SecurityCodeStatus.REVOKED) {
                    throw new IllegalArgumentException("This security code was revoked by an administrator.");
                }
                if (sc.getExpiresAt().isBefore(Instant.now()) || sc.getStatus() == SecurityCodeStatus.EXPIRED) {
                    throw new IllegalArgumentException("This security code has expired.");
                }
                return sc;
            }
        }
        throw new IllegalArgumentException("Invalid security code.");
    }
    
    private String randomBlock(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(secureRandom.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }
        
}
