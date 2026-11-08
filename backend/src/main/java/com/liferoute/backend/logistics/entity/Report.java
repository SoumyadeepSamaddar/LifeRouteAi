package com.liferoute.backend.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id", nullable = false, length = 80)
    private String caseId;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "generated_by")
    private Long generatedBy;

    @Column(name = "generated_at", nullable = false, updatable = false)
    private Instant generatedAt;

    @PrePersist
    protected void onCreate() {
        this.generatedAt = Instant.now();
    }
}
