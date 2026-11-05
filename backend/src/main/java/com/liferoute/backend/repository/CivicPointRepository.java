package com.liferoute.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.liferoute.backend.entity.CivicPoint;

@Repository
public interface CivicPointRepository extends JpaRepository<CivicPoint, Long> {

	List<CivicPoint> findByUserIdOrderByAwardedAtDesc(Long userId);

    @Query("SELECT COALESCE(SUM(c.points), 0) FROM CivicPoint c WHERE c.userId = :userId")
    Integer getTotalPointsByUserId(@Param("userId") Long userId);
}
