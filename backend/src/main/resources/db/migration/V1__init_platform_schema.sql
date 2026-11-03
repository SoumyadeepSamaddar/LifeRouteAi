-- 1. Hospitals Table
CREATE TABLE hospitals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    registration_no VARCHAR(100) NOT NULL UNIQUE,
    hospital_type ENUM('GOVERNMENT', 'PRIVATE', 'TRUST') NOT NULL,
    licensed_organs VARCHAR(255) NOT NULL, -- Comma-separated: HEART,LUNG,LIVER,KIDNEY,PANCREAS,CORNEA
    contact_person VARCHAR(150) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    email VARCHAR(150) NOT NULL,
    emergency_phone VARCHAR(30) NOT NULL,
    address VARCHAR(300) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(15) NOT NULL,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    has_helipad BOOLEAN NOT NULL DEFAULT FALSE,
    has_trauma_center BOOLEAN NOT NULL DEFAULT FALSE,
    operation_theaters_count INT NOT NULL DEFAULT 1,
    nearest_airport VARCHAR(150),
    nearest_railway_station VARCHAR(150),
    status ENUM('PENDING_ACTIVATION', 'ACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'PENDING_ACTIVATION',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Users Table (Admin, Hospital Staff, Patient, Donor)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30),
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'HOSPITAL', 'PATIENT', 'DONOR') NOT NULL,
    staff_role ENUM('HOSPITAL_ADMIN', 'COORDINATOR', 'DOCTOR', 'DISPATCH') NULL,
    hospital_id BIGINT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    must_change_password BOOLEAN NOT NULL DEFAULT FALSE,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    locked_until TIMESTAMP NULL,
    activation_token_hash VARCHAR(255) NULL,
    activation_expires_at TIMESTAMP NULL,
    last_login_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_hospital FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Hospital Security Codes Table
CREATE TABLE security_codes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    code_masked VARCHAR(30) NOT NULL,   -- e.g., LR-****-92QX
    code_hash VARCHAR(255) NOT NULL,    -- BCrypt / SHA-256 hash of full code
    status ENUM('ACTIVE', 'USED', 'EXPIRED', 'REVOKED') NOT NULL DEFAULT 'ACTIVE',
    created_by BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_seccode_hospital FOREIGN KEY (hospital_id) REFERENCES hospitals(id),
    CONSTRAINT fk_seccode_creator FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. System Settings Table (Algorithm Weights, Shelf Life, Traffic Penalties)
CREATE TABLE system_settings (
    setting_key VARCHAR(100) PRIMARY KEY,
    setting_value VARCHAR(500) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    updated_by BIGINT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_settings_user FOREIGN KEY (updated_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Audit Logs Table (Immutable)
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    user_email VARCHAR(150),
    user_role VARCHAR(30),
    hospital_id BIGINT NULL,
    action_type ENUM('LOGIN', 'LOGIN_FAILED', 'LOGOUT', 'CREATE', 'UPDATE', 'MATCH', 'VIEW_PATIENT', 'SETTINGS', 'SECURITY_CODE') NOT NULL,
    target_object VARCHAR(150) NOT NULL,
    details TEXT,
    ip_address VARCHAR(64),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Civic Points Table (Algorithm 4)
CREATE TABLE civic_points (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type ENUM('REGISTRATION', 'CAMP_ATTENDANCE', 'REFERRAL', 'DONATION_PLEDGE') NOT NULL,
    points INT NOT NULL,
    remarks VARCHAR(255),
    awarded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_civic_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Default Algorithm & System Settings Seed (Matches Page A9)
INSERT INTO system_settings (setting_key, setting_value, category, description) VALUES
('weight.a1.blood', '0.30', 'MATCHING_WEIGHTS', 'Weight for blood group compatibility (a1)'),
('weight.a2.health', '0.25', 'MATCHING_WEIGHTS', 'Weight for clinical health compatibility (a2)'),
('weight.a3.distance', '0.20', 'MATCHING_WEIGHTS', 'Weight for distance/proximity (a3)'),
('weight.a4.urgency', '0.25', 'MATCHING_WEIGHTS', 'Weight for patient urgency score (a4)'),
('weight.wait_time_bonus', '0.10', 'MATCHING_WEIGHTS', 'Urgency weight bonus after 365 wait days'),
('shelflife.HEART.hours', '4', 'ORGAN_SHELF_LIFE', 'Safe cold ischemia time for Heart (hours)'),
('shelflife.LUNG.hours', '6', 'ORGAN_SHELF_LIFE', 'Safe cold ischemia time for Lung (hours)'),
('shelflife.LIVER.hours', '12', 'ORGAN_SHELF_LIFE', 'Safe cold ischemia time for Liver (hours)'),
('shelflife.KIDNEY.hours', '24', 'ORGAN_SHELF_LIFE', 'Safe cold ischemia time for Kidney (hours)'),
('shelflife.handover_buffer_mins', '30', 'ORGAN_SHELF_LIFE', 'Handover and prep buffer in minutes'),
('transport.air.min_distance_km', '50', 'TRANSPORT', 'Minimum distance (km) to evaluate air transport'),
('transport.traffic.LOW', '0', 'TRANSPORT', 'Traffic penalty in minutes for LOW congestion'),
('transport.traffic.MODERATE', '15', 'TRANSPORT', 'Traffic penalty in minutes for MODERATE congestion'),
('transport.traffic.SEVERE', '40', 'TRANSPORT', 'Traffic penalty in minutes for SEVERE congestion'),
('retry.hold_interval_mins', '10', 'RETRY_ALERTS', 'Scheduler retry interval in minutes for HOLD cases');