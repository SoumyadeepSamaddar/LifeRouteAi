package com.liferoute.backend.logistics.repository;

import com.liferoute.backend.logistics.entity.GreenCorridor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreenCorridorRepository extends JpaRepository<GreenCorridor, Long> {
}
