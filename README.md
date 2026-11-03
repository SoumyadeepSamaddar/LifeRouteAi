LifeRouteAI is a production-grade Biomedical AI platform engineered for intelligent organ allocation, emergency transit routing, and cross-hospital transplantation logistics. The backend is built on Spring Boot 3.3.4 and Java 17, strictly adhering to enterprise vertical layered architecture (Entity ->Repository -> DTO -> Service-> ServiceImpl -> Controller).

Technical Stack
Language & Runtime: Java 17

Framework: Spring Boot 3.3.4 (Spring Web, Spring Data JPA, Spring Security, Spring Validation, Spring Transaction)

Database & Persistence: MySQL 8, Hibernate 6, Flyway Migrations

Security & Auth: Spring Security (Stateful HTTP session management via JSESSIONID, BCrypt password hashing, RBAC, CSRF token handling, brute-force lockouts)

Tooling & Build: Apache Maven, Project Lombok, Spring Tool Suite (STS 4)


Architectural Implementation Status
1. Database Schema & Flyway Migration (V1__init_platform_schema.sql)
Automated schema migration baseline managing 6 relational tables:

hospitals: Geo-coordinates, trauma center flags, helipad access, organ licenses, and verification status.

users: Identity management across roles (ADMIN, HOSPITAL, PATIENT, DONOR), lockouts, and timestamps.

security_codes: One-way hashed hospital activation credentials with expiration dates.

system_settings: Real-time weights for allocation algorithms, shelf-life thresholds, and transit penalties.

audit_logs: Immutable audit trail tracking security and domain events.

civic_points: Civic contribution and incentive tracking for organ donation awareness.

2. Vertical Slice #1 — Authentication, Session Lifecycle & Auditing

Session-Based Authentication: Clean cookie-driven authentication issuing JSESSIONID with HTTP-only security and configurable expiration.

Brute-Force Protection: Account locking mechanism triggered after consecutive failed authentication attempts.

Session Monitoring: Dedicated /api/auth/ping endpoint to refresh active sessions and track heartbeat status.

Immutable Auditing: Event-driven audit service writing operational traces (IP address, user context, action type, target resource).

3. Vertical Slice #2 — Hospital Provisioning & One-Time Code Activation
Hospital Onboarding: Admin-managed provisioning of hospitals with automated verification status workflows.

Cryptographic Security Codes: Admin generation of one-time onboarding codes formatted as LR-XXXX-XXXX. Only salted BCrypt hashes and masked representations (LR-****-XXXX) are persisted.

Public Activation Workflow: Two-step validation flow enabling hospital administrators to claim institutional profiles and establish root credentials.

4. Application Configuration & Seeding
Security Configuration: Modular Spring Security filter chain enforcing Role-Based Access Control (ROLE_ADMIN, ROLE_HOSPITAL, ROLE_PATIENT, ROLE_DONOR), custom exception handlers (clean 401/403 JSON payloads), and CORS policies.

Automatic Admin Seeding: CommandLineRunner initializing system admin credentials (admin@liferoute.ai / Admin1234) on first boot if no admin account exists.

Global Exception Handling: @RestControllerAdvice converting constraint violations and domain errors into structured HTTP 400 responses.




Repository Directory Structure
backend/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── liferoute/
        │           ├── backend/
        │           │   ├── BackendApplication.java
        │           │   ├── config/
        │           │   │   ├── DataInitializer.java
        │           │   │   ├── GlobalExceptionHandler.java
        │           │   │   └── SecurityConfig.java
        │           │   ├── controller/
        │           │   │   ├── AdminHospitalController.java
        │           │   │   ├── AuthController.java
        │           │   │   ├── HealthController.java
        │           │   │   └── HospitalActivationController.java
        │           │   ├── dto/
        │           │   │   ├── request/
        │           │   │   │   ├── ActivateHospitalRequestDTO.java
        │           │   │   │   ├── LoginRequestDTO.java
        │           │   │   │   └── VerifyCodeRequestDTO.java
        │           │   │   └── response/
        │           │   │       ├── ApiMessageResponseDTO.java
        │           │   │       ├── SecurityCodeResponseDTO.java
        │           │   │       ├── UserProfileResponseDTO.java
        │           │   │       └── VerifyCodeResponseDTO.java
        │           │   ├── entity/
        │           │   │   ├── AuditLog.java
        │           │   │   ├── Hospital.java
        │           │   │   ├── SecurityCode.java
        │           │   │   └── User.java
        │           │   ├── repository/
        │           │   │   ├── AuditLogRepository.java
        │           │   │   ├── HospitalRepository.java
        │           │   │   ├── SecurityCodeRepository.java
        │           │   │   └── UserRepository.java
        │           │   ├── security/
        │           │   │   ├── CustomUserDetails.java
        │           │   │   └── CustomUserDetailsService.java
        │           │   └── service/
        │           │       ├── AuditService.java
        │           │       ├── AuthService.java
        │           │       ├── HospitalService.java
        │           │       ├── SecurityCodeService.java
        │           │       └── impl/
        │           │           ├── AuditServiceImpl.java
        │           │           ├── AuthServiceImpl.java
        │           │           ├── HospitalServiceImpl.java
        │           │           └── SecurityCodeServiceImpl.java
        │           └── domain/
        │               └── enums/
        │                   ├── AuditActionType.java
        │                   ├── CivicActivityType.java
        │                   ├── HospitalStatus.java
        │                   ├── HospitalType.java
        │                   ├── Role.java
        │                   ├── SecurityCodeStatus.java
        │                   └── StaffRole.java
        └── resources/
            ├── application.yml
            └── db/
                └── migration/
                    └── V1__init_platform_schema.sql



