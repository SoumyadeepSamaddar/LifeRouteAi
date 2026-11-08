package com.liferoute.backend.logistics.repository;

import com.liferoute.backend.logistics.entity.HazardFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HazardFlagRepository extends JpaRepository<HazardFlag, Long> {
}
