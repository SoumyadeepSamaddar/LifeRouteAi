package com.liferoute.backend.service;

import java.util.List;

import com.liferoute.backend.dto.request.CreateHospitalStaffRequestDTO;
import com.liferoute.backend.dto.response.UserProfileResponseDTO;
import com.liferoute.backend.security.CustomUserDetails;

public interface HospitalStaffService {

	UserProfileResponseDTO createStaffMember(CreateHospitalStaffRequestDTO request, CustomUserDetails currentUser, String ipAddress);
    List<UserProfileResponseDTO> getHospitalStaff(Long hospitalId, CustomUserDetails currentUser);
}

