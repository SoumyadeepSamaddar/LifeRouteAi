package com.liferoute.backend.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.liferoute.domain.enums.HospitalStatus;
import com.liferoute.domain.enums.HospitalType;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "hospitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "registration_no", nullable = false, unique = true, length = 100)
    private String registrationNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "hospital_type", nullable = false)
    private HospitalType hospitalType;

    @Column(name = "licensed_organs", nullable = false)
    private String licensedOrgans;

    @Column(name = "contact_person", nullable = false, length = 150)
    private String contactPerson;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "emergency_phone", nullable = false, length = 30)
    private String emergencyPhone;

    @Column(nullable = false, length = 300)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 15)
    private String pincode;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Builder.Default
    @Column(name = "has_helipad", nullable = false)
    private boolean hasHelipad = false;

    @Builder.Default
    @Column(name = "has_trauma_center", nullable = false)
    private boolean hasTraumaCenter = false;

    @Builder.Default
    @Column(name = "operation_theaters_count", nullable = false)
    private int operationTheatersCount = 1;

    @Column(name = "nearest_airport", length = 150)
    private String nearestAirport;

    @Column(name = "nearest_railway_station", length = 150)
    private String nearestRailwayStation;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HospitalStatus status = HospitalStatus.PENDING_ACTIVATION;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        if (this.status == null) {
            this.status = HospitalStatus.PENDING_ACTIVATION;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
