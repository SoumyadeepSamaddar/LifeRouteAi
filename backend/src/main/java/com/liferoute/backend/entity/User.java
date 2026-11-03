package com.liferoute.backend.entity;

import java.time.Instant;

import com.liferoute.domain.enums.Role;
import com.liferoute.domain.enums.StaffRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, length = 150)
    private String name;
	
	@Column(nullable = false, unique = true, length = 150)
    private String email;
	
	@Column(length = 30)
    private String phone;
	
	@Column(name = "password_hash", nullable = false)
    private String passwordHash;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "staff_role")
    private StaffRole staffRole;
	
	@Column(name = "hospital_id")
    private Long hospitalId;
	
	@Column(name = "is_active", nullable = false)
    private boolean active = true;
	
	@Column(name = "must_change_password", nullable = false)
    private boolean mustChangePassword = false;
	
	@Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;
	
	@Column(name = "locked_until")
    private Instant lockedUntil;
	
	@Column(name = "activation_token_hash")
    private String activationTokenHash;
	
	@Column(name = "activation_expires_at")
    private Instant activationExpiresAt;
	
	@Column(name = "last_login_at")
    private Instant lastLoginAt;
	
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
