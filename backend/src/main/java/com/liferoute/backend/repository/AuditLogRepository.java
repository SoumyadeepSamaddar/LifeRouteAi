package com.liferoute.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liferoute.backend.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	List<AuditLog> findTop100ByOrderByCreatedAtDesc();
    List<AuditLog> findByHospitalIdOrderByCreatedAtDesc(Long hospitalId);
}
