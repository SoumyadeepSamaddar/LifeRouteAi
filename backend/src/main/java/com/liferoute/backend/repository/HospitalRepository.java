package com.liferoute.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liferoute.backend.entity.Hospital;
import com.liferoute.domain.enums.HospitalStatus;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
	List<Hospital> findByStatus(HospitalStatus status);
    boolean existsByRegistrationNo(String registrationNo);
}
