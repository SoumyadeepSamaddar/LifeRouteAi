package com.liferoute.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liferoute.backend.entity.Hospital;
import com.liferoute.backend.repository.HospitalRepository;
import com.liferoute.backend.security.CustomUserDetails;
import com.liferoute.backend.service.AuditService;
import com.liferoute.backend.service.HospitalService;
import com.liferoute.domain.enums.AuditActionType;
import com.liferoute.domain.enums.HospitalStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

	private final HospitalRepository hospitalRepository;
    private final AuditService auditService;
    
    @Override
    @Transactional(readOnly = true)
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found with ID: " + id));
    }
    
    @Override
    @Transactional
    public Hospital createHospital(Hospital hospital, CustomUserDetails admin, String ipAddress) {
        if (hospitalRepository.existsByRegistrationNo(hospital.getRegistrationNo())) {
            throw new IllegalArgumentException("Hospital registration number already exists.");
        }
        hospital.setStatus(HospitalStatus.PENDING_ACTIVATION);
        Hospital saved = hospitalRepository.save(hospital);
        
        auditService.log(admin, AuditActionType.CREATE, "Hospital:" + saved.getId(),
                "Created hospital " + saved.getName(), ipAddress);
        return saved;
    }
    
    @Override
    @Transactional
    public Hospital updateHospitalStatus(Long id, HospitalStatus status, CustomUserDetails admin, String ipAddress) {
        Hospital hospital = getHospitalById(id);
        hospital.setStatus(status);
        Hospital saved = hospitalRepository.save(hospital);
        
        auditService.log(admin, AuditActionType.UPDATE, "Hospital:" + id,
                "Updated hospital status to " + status, ipAddress);
        return saved;
    }
}
