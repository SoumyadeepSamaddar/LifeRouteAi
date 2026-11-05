# Life Root AI — Platform & Governance Foundation (Developer 1 Scope)

Life Root AI is an enterprise-grade biomedical platform engineered for intelligent organ allocation, emergency transit routing, and cross-hospital transplantation logistics. This repository contains the complete implementation of Developer 1's vertical slice, covering platform security, database architecture, administrative governance, and the shared React frontend design system.

---

## 1. Technical Stack & Architecture

* **Backend:** Java 17+, Spring Boot 3.3.4 (Spring Web, Spring Data JPA, Spring Security, Validation, Transaction)
* **Frontend:** React 18 + Vite, React Router v6, Axios with credentials, Custom Design System (`tokens.css`)
* **Database & Persistence:** MySQL 8, Hibernate 6, Flyway Migrations, Spring Session JDBC
* **Security & Auth:** Stateful HTTP session management via `JSESSIONID` cookie, BCrypt password hashing, Role-Based Access Control (RBAC), and CSRF protection (No JWT).

---

## 2. Database Schema (Flyway Base Migration)

The platform baseline (`V1__init_platform_schema.sql`) establishes 6 core tables:
1. **`hospitals`** — Transplant center profiles, location coordinates, trauma status, and organ licenses.
2. **`users`** — Unified user management with granular roles (`ADMIN`, `HOSPITAL`, `PATIENT`, `DONOR`) and hospital staff sub-roles (`COORDINATOR`, `DOCTOR`, `DISPATCH`, `HOSPITAL_ADMIN`).
3. **`security_codes`** — Cryptographically hashed, single-use activation keys for hospital onboarding.
4. **`system_settings`** — Dynamic key-value store for algorithm weights (a1–a4), organ shelf life, and traffic penalties.
5. **`audit_logs`** — Immutable compliance log tracking every authentication, creation, update, and administrative action.
6. **`civic_points`** — Algorithm 4 scoring engine tracking donor pledges and camp attendance.

---

## 3. Core Features Implemented (Developer 1)

* **Cryptographic Hospital Onboarding:** Admins generate secure one-time activation codes (e.g., `LR-XXXX-XXXX`) valid for configurable hours. Hospitals activate their institutional accounts using these codes.
* **Role-Based Security & Session Governance:** Stateful session handling with a 30-minute idle timeout, `/auth/ping` heartbeat, brute-force lockout protection (5 failed attempts), and an immutable audit trail.
* **Global Design System:** Strict adherence to project design tokens (`tokens.css`), featuring Root Teal (`#0F766E`), Life Red (`#E11D48`), Inter typography, and modular UI components (`Button`, `Card`, `StatCard`, `Badge`, `Input`, `Select`, `Modal`).
* **Admin Command Center & Portals:** Comprehensive views for hospital management, security code auditing, algorithm weight configurations, staff provisioning, and civic score tracking.

---

## 4. Getting Started Locally

### Backend Setup
1. Create a local MySQL database named `liferoot_db`.
2. Configure your credentials in `backend/src/main/resources/application.yml`.
3. Run the backend using the Maven wrapper:
   ```bash
   cd backend
   mvnw.cmd spring-boot:run