package com.liferoute.backend.service;

import com.liferoute.backend.dto.request.LoginRequestDTO;
import com.liferoute.backend.dto.response.UserProfileResponseDTO;
import com.liferoute.backend.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	UserProfileResponseDTO login(LoginRequestDTO loginRequest, HttpServletRequest request);
    UserProfileResponseDTO getCurrentUserProfile(CustomUserDetails currentUser);
    void logout(CustomUserDetails currentUser, HttpServletRequest request, HttpServletResponse response);
}
