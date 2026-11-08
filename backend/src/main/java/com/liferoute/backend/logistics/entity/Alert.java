package com.liferoute.backend.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_type", nullable = false, length = 40)
    private String alertType;

    @Column(name = "channel", nullable = false, length = 20)
    private String channel;

    @Column(name = "recipient", nullable = false, length = 255)
    private String recipient;

    @Column(name = "case_id", length = 80)
    private String caseId;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "delivery_status", nullable = false, length = 20)
    @Builder.Default
    private String deliveryStatus = "PENDING";

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
