package com.liferoute.backend.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transport_legs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportLeg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "leg_order", nullable = false)
    private Integer legOrder;

    @Column(name = "mode", nullable = false, length = 20)
    private String mode;

    @Column(name = "from_name", nullable = false, length = 150)
    private String fromName;

    @Column(name = "to_name", nullable = false, length = 150)
    private String toName;

    @Column(name = "distance_km", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal distanceKm = BigDecimal.ZERO;

    @Column(name = "duration_minutes", nullable = false)
    @Builder.Default
    private Integer durationMinutes = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
