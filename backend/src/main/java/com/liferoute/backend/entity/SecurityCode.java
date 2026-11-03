package com.liferoute.backend.entity;


import java.time.Instant;

import com.liferoute.domain.enums.SecurityCodeStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "security_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityCode {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Column(name = "code_masked", nullable = false, length = 30)
    private String codeMasked;

    @Column(name = "code_hash", nullable = false)
    private String codeHash;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SecurityCodeStatus status = SecurityCodeStatus.ACTIVE;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = SecurityCodeStatus.ACTIVE;
        }
    }
}
