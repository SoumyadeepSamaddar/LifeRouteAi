package com.liferoute.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liferoute.backend.entity.SecurityCode;
import com.liferoute.domain.enums.SecurityCodeStatus;

@Repository
public interface SecurityCodeRepository extends JpaRepository<SecurityCode, Long> {
	List<SecurityCode> findByStatus(SecurityCodeStatus status);
    List<SecurityCode> findByHospitalIdOrderByCreatedAtDesc(Long hospitalId);
}
