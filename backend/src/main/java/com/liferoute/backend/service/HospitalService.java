package com.liferoute.backend.service;

import java.util.List;

import com.liferoute.backend.entity.Hospital;
import com.liferoute.backend.security.CustomUserDetails;
import com.liferoute.domain.enums.HospitalStatus;

public interface HospitalService {
	List<Hospital> getAllHospitals();
    Hospital getHospitalById(Long id);
    Hospital createHospital(Hospital hospital, CustomUserDetails admin, String ipAddress);
    Hospital updateHospitalStatus(Long id, HospitalStatus status, CustomUserDetails admin, String ipAddress);
}

