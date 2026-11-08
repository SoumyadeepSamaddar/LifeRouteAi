package com.liferoute.backend.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "hazard_flags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HazardFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "hazard_type", nullable = false, length = 40)
    private String hazardType;

    @Column(name = "severity", nullable = false, length = 20)
    private String severity;

    @Column(name = "area", nullable = false, length = 150)
    private String area;

    @Column(name = "affected_roads", length = 255)
    private String affectedRoads;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;

    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private String status = "REQUESTED";

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "approved_by")
    private Long approvedBy;

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
