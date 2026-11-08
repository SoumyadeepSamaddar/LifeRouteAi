CREATE TABLE routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    match_case_id VARCHAR(80) NULL,
    donor_hospital_id BIGINT NULL,
    recipient_hospital_id BIGINT NULL,
    organ_type VARCHAR(40) NOT NULL,
    route_status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    selected_mode VARCHAR(20) NULL,
    backup_mode VARCHAR(20) NULL,
    total_distance_km DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_eta_minutes INT NOT NULL DEFAULT 0,
    viable BOOLEAN NOT NULL DEFAULT FALSE,
    reason TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_routes_donor_hospital FOREIGN KEY (donor_hospital_id) REFERENCES hospitals(id),
    CONSTRAINT fk_routes_recipient_hospital FOREIGN KEY (recipient_hospital_id) REFERENCES hospitals(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE transport_legs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    leg_order INT NOT NULL,
    mode VARCHAR(20) NOT NULL,
    from_name VARCHAR(150) NOT NULL,
    to_name VARCHAR(150) NOT NULL,
    distance_km DECIMAL(10, 2) NOT NULL DEFAULT 0,
    duration_minutes INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transport_route FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE hazard_flags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    hazard_type VARCHAR(40) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    area VARCHAR(150) NOT NULL,
    affected_roads VARCHAR(255) NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'REQUESTED',
    created_by BIGINT NULL,
    approved_by BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_hazard_created_by FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_hazard_approved_by FOREIGN KEY (approved_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE green_corridors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'REQUESTED',
    police_contact VARCHAR(150) NULL,
    reference_note TEXT NULL,
    requested_by BIGINT NULL,
    validated_by BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_green_route FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    CONSTRAINT fk_green_requested_by FOREIGN KEY (requested_by) REFERENCES users(id),
    CONSTRAINT fk_green_validated_by FOREIGN KEY (validated_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    alert_type VARCHAR(40) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient VARCHAR(255) NOT NULL,
    case_id VARCHAR(80) NULL,
    payload TEXT NULL,
    delivery_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    read_at TIMESTAMP NULL,
    error_message TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    case_id VARCHAR(80) NOT NULL,
    title VARCHAR(150) NOT NULL,
    url VARCHAR(255) NULL,
    generated_by BIGINT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_user FOREIGN KEY (generated_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
