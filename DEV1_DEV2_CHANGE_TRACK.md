# Dev 1 / Dev 2 Change Tracker

## Purpose
This file records the exact shared and integration-facing changes that affect the Dev 1 and Dev 2 teams during the Dev 3 logistics implementation.

## 1) Dev 1-owned/shared changes

The Dev 3 work intentionally touched only the shared platform integration points required to expose the transport module safely.

- `backend/src/main/java/com/liferoute/backend/config/SecurityConfig.java`
  - Added authenticated access for `/api/transport/**`
  - Kept the rest of the Dev 1 security model intact
- `backend/src/main/resources/application.yml`
  - Added placeholders for map provider and notification provider configuration
- `frontend/src/App.jsx`
  - Registered `/transport` and `/matches/:matchId` routes
- `frontend/src/components/layout/AppLayout.jsx`
  - Added navigation links for the transport pages
- `frontend/package.json`
  - Added frontend packages required for map and real-time UI support

## 2) What Dev 3 did not change in Dev 1 code

- No existing admin pages were rewritten
- No core platform auth workflow was replaced
- No shared UI shell design tokens were changed beyond adding navigation and route registration

## 3) Dev 2 integration points and dependency status

The Dev 3 transport layer depends on contract inputs that are still expected from Dev 2.

### Required real contracts still pending
- real patient / donor data entities and repositories
- actual match-case records and queue status model
- donor retrieval timestamps and organ ischemia fields
- hospital ownership and route access rules
- final match detail or case detail API contract

### Purpose of the current Dev 3 placeholders
- route viability is evaluated using mock donor/recipient coordinates and route assumptions
- route simulation uses a lightweight DTO contract based on donor and recipient coordinates
- the UI uses mock match data until the actual Dev 2 records are connected

## 4) Files currently used as integration boundaries

- `backend/src/main/java/com/liferoute/backend/logistics/dto/SimulationRequestDTO.java`
- `backend/src/main/java/com/liferoute/backend/logistics/dto/SimulationResponseDTO.java`
- `backend/src/main/java/com/liferoute/backend/logistics/controller/TransportController.java`
- `backend/src/main/java/com/liferoute/backend/logistics/service/impl/SimulationServiceImpl.java`

## 5) Current status

- Dev 1 and Dev 2 domain files were not overwritten or heavily modified
- Dev 3 remains isolated to its transport/logistics modules
- Shared integration points are documented and ready for handoff
- Production-grade wiring to the real Dev 2 domain contracts remains the next step
- Local backend runtime compatibility was improved by adding a safe H2 fallback for development when MySQL is not running, while preserving MySQL override support through environment variables

## 6) Recommended next collaboration items

1. Dev 2: provide the concrete match-case and candidate APIs
2. Dev 1: confirm access and approval workflow for green corridor and hazard actions
3. Dev 3: replace mock route + simulation data with live real-case values when contracts are available
