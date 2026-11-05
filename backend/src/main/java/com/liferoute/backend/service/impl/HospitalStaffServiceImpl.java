package com.liferoute.backend.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liferoute.backend.dto.request.CreateHospitalStaffRequestDTO;
import com.liferoute.backend.dto.response.UserProfileResponseDTO;
import com.liferoute.backend.entity.AuditLog;
import com.liferoute.backend.entity.Hospital;
import com.liferoute.backend.entity.User;
import com.liferoute.backend.repository.AuditLogRepository;
import com.liferoute.backend.repository.HospitalRepository;
import com.liferoute.backend.repository.UserRepository;
import com.liferoute.backend.security.CustomUserDetails;
import com.liferoute.backend.service.HospitalStaffService;
import com.liferoute.domain.enums.AuditActionType;
import com.liferoute.domain.enums.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalStaffServiceImpl implements HospitalStaffService {
	
	private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogRepository auditLogRepository;
	
    @Override
    @Transactional
    public UserProfileResponseDTO createStaffMember(CreateHospitalStaffRequestDTO request, CustomUserDetails currentUser, String ipAddress) {
        Long targetHospitalId = (currentUser.getRole() == Role.ADMIN)
                ? request.getHospitalId()
                : currentUser.getHospitalId();

        if (targetHospitalId == null) {
            throw new IllegalArgumentException("Hospital ID must be provided to onboard hospital staff.");
        }
        
        Hospital hospital = hospitalRepository.findById(targetHospitalId)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found with ID: " + targetHospitalId));

        if (userRepository.existsByEmailIgnoreCase(request.getEmail().trim())) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }
        
        User staffUser = User.builder()
                .name(request.getName().trim())
                .email(request.getEmail().trim().toLowerCase())
                .phone(request.getPhone().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.HOSPITAL)
                .staffRole(request.getStaffRole())
                .hospitalId(hospital.getId())
                .active(true)
                .mustChangePassword(true)
                .failedLoginAttempts(0)
                .build();

        User saved = userRepository.save(staffUser);
        
        AuditLog log = AuditLog.builder()
                .userId(currentUser.getId())
                .userEmail(currentUser.getEmail())
                .userRole(currentUser.getRole().name())
                .hospitalId(hospital.getId())
                .actionType(AuditActionType.CREATE)
                .targetObject("HospitalStaff:" + saved.getEmail())
                .details("Created hospital staff with role " + saved.getStaffRole() + " for " + hospital.getName())
                .ipAddress(ipAddress)
                .createdAt(Instant.now())
                .build();
        auditLogRepository.save(log);

        return mapToProfileDTO(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponseDTO> getHospitalStaff(Long hospitalId, CustomUserDetails currentUser) {
        Long targetHospitalId = (currentUser.getRole() == Role.ADMIN && hospitalId != null)
                ? hospitalId
                : currentUser.getHospitalId();

        if (targetHospitalId == null) {
            throw new IllegalArgumentException("Hospital ID is required.");
        }

        return userRepository.findByHospitalIdOrderByCreatedAtDesc(targetHospitalId)
                .stream()
                .map(this::mapToProfileDTO)
                .collect(Collectors.toList());
    }
    
    private UserProfileResponseDTO mapToProfileDTO(User user) {
        return UserProfileResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .staffRole(user.getStaffRole())
                .hospitalId(user.getHospitalId())
                .mustChangePassword(user.isMustChangePassword())
                .build();
    }
}
