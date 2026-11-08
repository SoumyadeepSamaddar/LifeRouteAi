package com.liferoute.backend.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "green_corridors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GreenCorridor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private String status = "REQUESTED";

    @Column(name = "police_contact", length = 150)
    private String policeContact;

    @Column(name = "reference_note", columnDefinition = "TEXT")
    private String referenceNote;

    @Column(name = "requested_by")
    private Long requestedBy;

    @Column(name = "validated_by")
    private Long validatedBy;

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
