# Dev 3 Logistics / Transport Handoff

## 0) Change tracker for Dev 1 / Dev 2 / Dev 3

This document tracks the shared touchpoints and isolation boundaries for the current implementation.

### What was changed in Dev 1-owned/shared code
- `backend/src/main/java/com/liferoute/backend/config/SecurityConfig.java`
  - allowed authenticated access to `/api/transport/**`
- `backend/src/main/resources/application.yml`
  - added transport/map/notification config placeholders
- `frontend/src/App.jsx`
  - registered transport and match detail routes
- `frontend/src/components/layout/AppLayout.jsx`
  - added transport navigation items
- `frontend/package.json`
  - added route/map/realtime dependencies

### What was intentionally not changed in Dev 1/shared code
- No existing admin or shared platform workflows were rewritten.
- No Dev 1 security model was changed beyond the transport access requirement.

### What was changed in Dev 2-facing integration points
- Dev 3 added route viability and simulation contracts that expect donor/recipient coordinates and match identifiers.
- The transport slice remains mock-backed until real match-case and patient/donor data contracts are supplied by Dev 2.
- No Dev 2 clinical tables or matching logic were modified.

### What Dev 3 added
- Full logistics backend package and Flyway tables
- Route viability, hazard, alert, and live tracking features
- Frontend transport and match detail screens
- Route simulation endpoint support using `SimulationRequestDTO`

---

## 1) Scope and ownership

This project uses a vertical-slice ownership model:

- Dev 1 owns platform, security, admin, shared UI, generic app shell
- Dev 2 owns clinical/matching domain logic, patient/donor records, queue and match engine
- Dev 3 owns logistics, route viability, transport options, green corridor, alerts, hazard handling, live tracking

This handoff records what Dev 3 created and what must still be completed by the other teams.

---

## 2) What Dev 3 changed in shared / existing code

### Shared backend changes

Files touched in the existing Dev 1 platform layer:

- `backend/src/main/java/com/liferoute/backend/config/SecurityConfig.java`
  - Added authenticated access for `/api/transport/**` so the logistics layer can be reached by logged-in users.
- `backend/src/main/resources/application.yml`
  - Added config placeholders for:
    - `app.maps.provider`
    - `app.notifications.provider`
    - server-side map and notification configuration support

### Shared frontend changes

Files touched in the existing Dev 1 / shared UI layer:

- `frontend/src/App.jsx`
  - Added Dev 3 routes for:
    - `/transport`
    - `/matches/:matchId`
- `frontend/src/components/layout/AppLayout.jsx`
  - Added navigation for Transport and Match Detail pages
- `frontend/package.json`
  - Added required packages for maps and realtime support
- `frontend/src/pages/transport/TransportPage.jsx`
  - New Dev 3 page
- `frontend/src/pages/transport/MatchDetailPage.jsx`
  - New Dev 3 match-detail screen
- `frontend/src/pages/transport/VehicleTrackingCard.jsx`
  - New live tracking display widget

No domain logic from Dev 1 or Dev 2 was rewritten. We kept the existing structure and only added the logistics layer around it.

---

## 3) Dev 3 additions

### Backend

Created under `backend/src/main/java/com/liferoute/backend/logistics/`:

- `controller/`
  - `TransportController.java`
  - `GreenCorridorController.java`
  - `HazardFlagController.java`
  - `AlertController.java`
  - `VehicleLocationController.java`
- `entity/`
  - `Route.java`
  - `TransportLeg.java`
  - `HazardFlag.java`
  - `GreenCorridor.java`
  - `Alert.java`
  - `Report.java`
  - `VehicleLocation.java`
- `repository/`
  - `RouteRepository.java`
  - `TransportLegRepository.java`
  - `HazardFlagRepository.java`
  - `GreenCorridorRepository.java`
  - `AlertRepository.java`
  - `ReportRepository.java`
  - `VehicleLocationRepository.java`
- `service/`
  - `MapsClient.java`
  - `NotificationService.java`
  - `ViabilityService.java`
  - `TransportService.java`
  - `HazardFlagService.java`
  - `AlertService.java`
- `service/impl/`
  - `MockMapsClient.java`
  - `GoogleMapsClient.java`
  - `LoggingNotificationService.java`
  - `SmtpNotificationService.java`
  - `ViabilityServiceImpl.java`
  - `TransportServiceImpl.java`
  - `HazardFlagServiceImpl.java`
  - `AlertServiceImpl.java`
- `dto/`
  - `ViabilityRequestDTO.java`
  - `ViabilityResponseDTO.java`
  - `RouteOptionDTO.java`
  - `TransportRequestDTO.java`
  - `TransportResponseDTO.java`
  - `GreenCorridorRequestDTO.java`

### Database migrations

- `backend/src/main/resources/db/migration/V2__dev3_transport_tables.sql`
  - `routes`
  - `transport_legs`
  - `hazard_flags`
  - `green_corridors`
  - `alerts`
  - `reports`
- `backend/src/main/resources/db/migration/V3__dev3_vehicle_tracking.sql`
  - `vehicle_locations`

### Frontend

- `frontend/src/pages/transport/TransportPage.jsx`
- `frontend/src/pages/transport/MatchDetailPage.jsx`
- `frontend/src/pages/transport/VehicleTrackingCard.jsx`

### Config templates

- `backend/.env.example`
- `frontend/.env.example`

### Local runtime compatibility note

The app was originally configured for a local MySQL instance, but the environment used for local development did not have MySQL running. To keep Dev 3 work runnable without breaking the intended MySQL production setup, the backend now defaults to an H2 in-memory database for local startup while allowing project-level override with `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `FLYWAY_ENABLED=true` when a MySQL instance is available.

This is a Dev 3 operational compatibility fix, not a change to the platform/domain ownership boundaries.

---

## 4) Endpoint summary added by Dev 3

### Transport

- `GET /api/transport/health`
- `POST /api/transport/viability`
- `POST /api/transport/options`
- `GET /api/transport/hazards`
- `POST /api/transport/hazards`
- `PATCH /api/transport/hazards/{id}/status`
- `GET /api/transport/alerts`
- `POST /api/transport/alerts`
- `PATCH /api/transport/alerts/{id}/read`
- `POST /api/transport/green-corridor`
- `PATCH /api/transport/green-corridor/{id}/status`
- `GET /api/transport/vehicle-locations`
- `POST /api/transport/vehicle-locations`

These are intentionally lightweight operational scaffolds so the rest of the app can start using transport logic before the complete Dev 2 match engine is connected.

---

## 5) Requests for Dev 1 / Dev 2

### For Dev 1

1. Finalize and expose the real Admin approval flow for hazard flags.
2. Confirm whether any reference tables for police contacts, helipads, airports, or railway corridors already exist, or create them if missing.
3. Keep audit logging for patient-data view events in the existing audit service / audit log flow.
4. Decide if route / transport page access should be restricted by hospital ownership and role in the final security policies.
5. Provide final admin UI for green corridor validation / approval if that is part of the admin slice.

### For Dev 2

1. Provide the actual patient, donor, match-case, and queue entities and repositories so the route engine can reference real match IDs and organ records.
2. Expose a real match detail API (or share the existing service contract) so Dev 3 can replace the current mock route/test data with real case data.
3. Provide donor retrieval time / organ retrieval timestamp field(s) for ischemia-time logic.
4. Share the exact match case status model and route selection callback hooks.
5. Confirm whether route viability should be calculated against the real match candidate list and whether a route record should belong to a match case or candidate record.

---

## 6) Important note

The Dev 3 logistics layer is intentionally isolated and non-breaking. It does not modify Dev 1’s security model beyond making transport endpoints accessible to authenticated users, and it does not rewrite Dev 2’s clinical logic.

We have not altered the core Dev 1 admin pages or Dev 2 patient/matching domain implementations beyond the required shared routing and config changes.

---

## 7) Validation status

Verified locally:

- Frontend production build passed
- Backend compile passed

---

## 8) Next recommended work

1. Connect the transport page to actual match-case data from Dev 2.
2. Add real route map rendering with donor/recipient/vehicle markers.
3. Add a simulate-along-route endpoint for tracking tests.
4. Finalize hazard approval and green corridor validation workflow with Dev 1.
