package com.liferoute.backend.logistics.repository;

import com.liferoute.backend.logistics.entity.TransportLeg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportLegRepository extends JpaRepository<TransportLeg, Long> {
}
