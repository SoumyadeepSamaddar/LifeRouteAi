package com.liferoute.backend.entity;

import java.time.Instant;

import com.liferoute.domain.enums.CivicActivityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "civic_points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CivicPoint {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private CivicActivityType activityType;
    
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "awarded_at")
    private Instant awardedAt;

    @PrePersist
    protected void onCreate() {
        if (this.awardedAt == null) {
            this.awardedAt = Instant.now();
        }
    }
}
