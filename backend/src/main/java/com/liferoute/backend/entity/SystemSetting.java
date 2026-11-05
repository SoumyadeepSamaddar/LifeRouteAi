package com.liferoute.backend.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "system_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemSetting {

	@Id
    @Column(name = "setting_key", length = 100, nullable = false)
    private String settingKey;
	
	@Column(name = "setting_value", length = 500, nullable = false)
    private String settingValue;

    @Column(name = "category", length = 50, nullable = false)
    private String category;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
