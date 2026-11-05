package com.liferoute.backend.service;

import java.util.List;

import com.liferoute.backend.dto.request.UpdateSystemSettingRequestDTO;
import com.liferoute.backend.dto.response.SystemSettingResponseDTO;

public interface SystemSettingService {

	List<SystemSettingResponseDTO> getAllSettings(String category);
    SystemSettingResponseDTO getSettingByKey(String key);
    SystemSettingResponseDTO updateSetting(String key, UpdateSystemSettingRequestDTO request, Long adminUserId, String ipAddress);
    double getDoubleValue(String key, double defaultValue);
    int getIntValue(String key, int defaultValue);
}
