package com.liferoute.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liferoute.backend.entity.SystemSetting;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, String> {
	List<SystemSetting> findByCategoryOrderBySettingKeyAsc(String category);
}
