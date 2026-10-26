# LifeRouteAI 🚑🫀

LifeRouteAI is an automated organ allocation, donor-recipient matching, and transit management platform. This repository contains the containerized multi-service environment and the initial Spring Boot backend setup.

---

## 🛠️ Current Tech Stack

* **Language:** Java 17 (OpenJDK / Eclipse Temurin)
* **Framework:** Spring Boot 3.3.4 (`com.liferoute:backend`)
* **Database:** MySQL 8.0 (Schema: `liferoot_db`)
* **Database Migrations:** Flyway Migration (MySQL extension enabled)
* **Build Tool:** Apache Maven (`mvnw` included)
* **Containerization:** Docker & Docker Compose
* **IDE Compatibility:** Spring Tool Suite (STS), Eclipse, IntelliJ IDEA, VS Code

---

## 📂 Active Project Structure

```text
Final LifeRouteAI/
├── docker-compose.yml          # Multi-container orchestration (DB, API, frontend, ML)
├── .gitignore                  # Excludes target/, IDE configs, .env, and logs
├── README.md                   # Project setup & architecture documentation
└── backend/                    # Core Spring Boot application
    ├── pom.xml                 # Maven dependencies & plugins
    ├── Dockerfile              # Multi-stage container build
    └── src/
        ├── main/
        │   ├── java/com/liferoute/backend/
        │   │   ├── BackendApplication.java    # Application entry point
        │   │   └── HealthController.java      # REST health verification endpoint
        │   └── resources/
        │       ├── application.yml            # Datasource, port, & Flyway configuration
        │       └── db/migration/
        │           └── V1__init_platform_schema.sql # Initial DB tables (recipients, donors, matches)
        └── test/                              # Application test suite