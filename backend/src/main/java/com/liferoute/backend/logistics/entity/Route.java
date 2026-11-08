package com.liferoute.backend.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_case_id", length = 80)
    private String matchCaseId;

    @Column(name = "donor_hospital_id")
    private Long donorHospitalId;

    @Column(name = "recipient_hospital_id")
    private Long recipientHospitalId;

    @Column(name = "organ_type", nullable = false, length = 40)
    private String organType;

    @Column(name = "route_status", nullable = false, length = 30)
    @Builder.Default
    private String routeStatus = "DRAFT";

    @Column(name = "selected_mode", length = 20)
    private String selectedMode;

    @Column(name = "backup_mode", length = 20)
    private String backupMode;

    @Column(name = "total_distance_km", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalDistanceKm = BigDecimal.ZERO;

    @Column(name = "total_eta_minutes", nullable = false)
    @Builder.Default
    private Integer totalEtaMinutes = 0;

    @Column(name = "viable", nullable = false)
    @Builder.Default
    private Boolean viable = false;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
