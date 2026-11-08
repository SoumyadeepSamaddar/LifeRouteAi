Life Root AI — Project Work Report
Development blueprint for the 3-developer team
Version 1.0 · 25 October 2026 · Stack: Spring Boot, React JS, JPA/Hibernate, MySQL · Authentication: session-based (no JWT)
0. How to Use This Document
This report is the single reference for building the Life Root AI website. Read it in this order:
Section 3 tells you who builds what.
Section 7 gives the global design rules (colors, fonts, buttons) that every page must follow.
Section 8 lists every page: its route, who can open it, what is on it, and which buttons it has.
Sections 9 to 11 give the database, the APIs and the algorithms.
Team rule: if something is unclear or changes, update this document first, then write code. This keeps all three developers working on the same idea.
1. Project Overview
1.1 Summary
Item
Detail
Project name
Life Root AI
What it is
A web platform that matches organ donors with patients using AI scoring, checks that the organ can reach the patient in safe time, finds the fastest route (road, air, train), tracks the green corridor and alerts everyone involved
Main users
Admin, Hospital team, Patient / Donor
Core promise
Less waiting, less manual work, and a clear explanation of why a match was made
Important note
The system is a decision-support tool. The final medical decision always stays with the transplant doctors.

1.2 Problem Statement (short)
Today organ allocation and transfer face four main problems:
Manual cross-matching delays. Compatibility is checked through static registries and phone calls between hospitals. This uses up ischemia time (about 4–6 hours for heart and lungs).
Poor communication and transport delays. Transit teams use general navigation apps that do not share live information between hospitals and do not see traffic jams, events or strikes.
Outdated matching. First-come-first-served or fixed scoring rules do not predict real compatibility or how well the patient will recover after transplant.
Information gaps. No central alert reaches nearby trauma centers and waiting hospitals at the moment a donor becomes available.
Life Root AI solves these with ML-assisted compatibility and survival scoring, and the quickest route between donor and recipient theaters.
1.3 Objectives
Primary
Build an AI-driven matching and transfer framework that checks donor–recipient compatibility using medical and urgency parameters.
Score and match donors and patients using blood group, age, organ type and medical emergency level.
Use maps to calculate the fastest route and improve transplant success.
Replace paper-based, scattered workflows with automation to reduce errors and delay.
Secondary (value-added)
Send instant SMS and email alerts to staff and surgical teams when a match is confirmed.
Track the exact organ travel time and keep it inside the safe limit.
Create a PDF report of every matching process for regulation and audit.
Check express train / metro availability as a transport option.
Validate and track the emergency green corridor.
1.4 Scope
In scope (version 1)
Admin, hospital and patient/donor logins, each with its own dashboard.
Hospital activation using an admin-generated security code.
Patient and donor data entry, urgency scoring, priority queue.
Matching engine (Algorithms 1–3) with a clear 'why matched' explanation.
Distance, travel-time and viability check; multi-mode route (road, air, train) with a backup route.
Uber-style map showing both locations and the route in detail.
Green corridor request tracking, alerts (SMS, email, in-app) and PDF report.
Hold state when no match is found, with automatic retry.
Civic score (Algorithm 4) on the user profile.
Out of scope for version 1 (future)
Direct integration with national organ registries, railway systems and traffic police systems. Version 1 uses admin-maintained data and notifications instead.
Native mobile apps. The website must be fully mobile responsive instead.
Replacing clinical judgement.
2. Technology Stack
Layer
Choice
Notes
Frontend framework
React 18 + Vite
JavaScript, functional components and hooks
Routing
React Router v6
Protected routes check the logged-in role
API calls
Axios with withCredentials: true
Sends the session cookie automatically
Server state
TanStack React Query
Loading, caching, refetch
Forms
React Hook Form + Yup
Inline validation, multi-step forms
Charts
Recharts
Score bars, scatter plot, time-series
Maps
Google Maps JavaScript API (@react-google-maps/api)
Markers, route line, live position
Realtime
WebSocket (STOMP over SockJS)
Match status and alerts; uses the same session cookie
Backend
Java 17+, Spring Boot 3.x
Layers: Controller → Service → Repository
Security
Spring Security with HTTP session
JSESSIONID cookie, CSRF protection, BCrypt passwords. No JWT.
Session store
Spring Session JDBC (MySQL)
Sessions survive a server restart (recommended)
ORM
Spring Data JPA + Hibernate
Entities, repositories, @Transactional services
Database
MySQL 8
utf8mb4, InnoDB, schema changes only through Flyway migrations
Validation
Jakarta Bean Validation
@Valid on every request DTO
Email / SMS
Spring Mail (SMTP); MSG91 or Twilio
Hidden behind a NotificationService interface
PDF
OpenPDF (or iText)
Match report generation
Maps (server side)
Google Directions, Distance Matrix, Geocoding
Called only from the backend so the API key stays secret
Scheduler
Spring @Scheduled
Hold retry, transit timers, expiry of codes
API docs
springdoc-openapi (Swagger UI)
API contract agreed before coding
Tools
Git/GitHub, Maven, Postman, Docker Compose (MySQL)
One shared repo with /backend and /frontend folders

About the AI part. Phase 1 uses the formulas and rules in Algorithms 1–4 inside Spring Boot, so the project works without a separate ML server. Phase 2 (optional) adds a trained model for predictedSuccessRate as a small Python (FastAPI) service. It is called through a PredictionService interface, so the rest of the application does not change when the model is upgraded.
3. Team Work Division
Principle: each developer owns a vertical slice (backend + frontend + database tables) of the product. This way nobody waits for another person for long. Shared items are built in Week 1.
3.1 Ownership table


Developer 1 — Platform, Security & Admin
Developer 2 — Clinical Data & Matching Engine
Developer 3 — Logistics, Maps & Alerts
Backend
Project setup, Flyway base schema, Spring Security session login/logout, CSRF and CORS, roles, security codes, hospital management, admin users, system settings, audit log, civic score, patient/donor portal APIs, global error handling
Patient and donor entities, clinical attributes, urgency score and star rating, priority queue, matching engine, hard filters, why-matched explanation, hold and retry, predicted success rate
Distance and viability filter, Google Maps integration, transport mode selection (road, air, train), hazard checks, backup route, ischemia timer, green corridor workflow, alerts (SMS, email, in-app), PDF report, WebSocket
Frontend
Design system and shared UI components, app layout (navbar, sidebar), login, hospital activation, all Admin pages, all Patient/Donor pages, 403/404 pages
Hospital dashboard, add/edit patient and donor forms, lists and detail pages, waiting queue, hold cases, match center and match detail (why-matched tab with graphs)
Transport page with Uber-style map, route details panel, green corridor panel, alerts center, report download, live timers, route tab inside match detail
Algorithms
Algorithm 4 (Civic score)
Algorithm 1 (Core matching) and Algorithm 3 (Priority queue)
Algorithm 2 (Distance and viability)
Main DB tables
users, hospitals, security_codes, system_settings, audit_logs, civic_points
patients, donors, clinical_profiles, queue_entries, match_cases, match_candidates
routes, transport_legs, hazard_flags, green_corridors, alerts, reports

3.2 Week-1 shared work
Task
Owner
Done when
Repo, folder structure, MySQL Docker, base Spring Boot and React apps
Developer 1
All three can run the project locally
Session login working end to end with a dummy user
Developer 1
Login → cookie → protected API → logout works
Design tokens (colors, fonts, spacing) and Button, Input, Card, Table, Badge, Modal components
Developer 1
Other developers import them from components/ui
OpenAPI contract for the matching and route APIs
Developer 2 and 3 together
Swagger file reviewed by all three
Seed data (3 hospitals, 10 patients, 5 donors, 2 admins)
Developer 2
Data script runs in one command

3.3 Hand-off points between developers
From
To
What is handed over
Developer 1
Developer 2 and 3
Logged-in user, role and hospitalId in the Spring Security context; shared UI components; settings API (weights, shelf life, buffers)
Developer 2
Developer 3
A match record with donor and patient coordinates, organ type and shelf life, ready for the viability and route check
Developer 3
Developer 2
Viability result (viable Y/N, ETA, buffer) for each candidate so the final allocation can be chosen
Developer 3
Developer 1
Alert events that appear in the patient/donor notification feed and the admin audit log

3.4 Working rules
One feature = one branch (feature/dev2-patient-form). Merge to develop through a pull request that another developer reviews.
Only the table owner changes that table, and only through a Flyway migration file (V3__add_hold_reason.sql).
Shared UI components live only in frontend/src/components/ui and are changed only by Developer 1 (others raise a request).
Every API is added to Swagger before the frontend uses it.
No page is finished until it has loading, empty and error states (see Section 7.8).
4. User Roles & Permissions
4.1 Roles
Role
Who
Notes
ADMIN
Platform owners
More than one admin is allowed. Admin can see everything and do everything.
HOSPITAL
Hospital team members
Each person has a staff role inside the hospital (below). A hospital sees only its own data.
PATIENT
Transplant recipient
Created by the hospital. Sees only own details and status.
DONOR
Living or pledged donor
Created by the hospital. Sees only own details and status.

Hospital staff roles (added so the different groups named in the scope get proper dashboards):
Hospital Admin — manages staff accounts of that hospital.
Coordinator / Organ procurement — adds patients and donors, runs matching, watches queues.
Doctor / Surgeon (medical expert) — confirms or rejects a match, adds medical notes.
Dispatch (delivery) — manages transport, route choice, green corridor and live tracking.
4.2 Permission matrix
Feature
Admin
Hospital
Patient / Donor
Create other admins
Yes
No
No
Generate security code
Yes
No
No
Add and edit hospitals
Yes
Edit own profile only (Hospital Admin)
No
Add hospital staff
Yes
Hospital Admin
No
Add patient / donor
Yes
Coordinator, Doctor
No
See patient and donor queue
All hospitals
Own hospital
No
Run or re-run matching
Yes
Coordinator, Doctor
No
Confirm or reject a match
Yes
Doctor
No
See why matched and route
Yes
Yes (cases involving own hospital)
Simple status view only
Manage transport and green corridor
Yes
Dispatch, Coordinator
No
Download PDF report
All
Own cases
No
Change algorithm settings
Yes
No
No
See audit log
All
Hospital Admin (own hospital)
No
See own details, status, notifications
n/a
n/a
Yes
See civic points
n/a
n/a
Yes

Privacy rule (important): a match often involves two different hospitals. Until a match is confirmed, the other hospital sees only an anonymous candidate (ID, scores, blood group, organ) and never name, phone or address. All views of patient data are written to the audit log.
5. End-to-End Workflow
5.1 The 7 steps
Step
What happens
Who
Screen
Status after step
1
Patient is admitted to the hospital and registered in Life Root AI
Hospital Coordinator
Add Patient
admissionStatus = ADMITTED
2
Hospital adds the patient health attributes (blood group, HLA, PRA, weight, organ needed, etc.)
Hospital
Add Patient (steps 2–4)
Profile complete
3
Algorithm calculates urgencyScore (1–100), gives AI star rating (3, 4 or 5 star) and queue tier (1st, 2nd, 3rd). Patient enters the priority queue.
System
Waiting Queue
ACTIVE
4
A donor is found and added. The organ is broadcast and the candidate list is filtered by hard rules, then ranked by the core score (high score first). The screen shows why each match was made.
Hospital / System
Match Center
MATCHING
5
Top candidates go through the distance and viability check. The patient who is high priority, near and viable gets the donor. The map shows both locations and the path in detail, like Uber.
System
Match Detail + Map
PENDING_CONFIRMATION
5b
Doctor confirmation (added step): the surgeon confirms or rejects the suggested match. This is the ethics and manual-override check.
Doctor
Match Detail
CONFIRMED
6
Best transport found by comparing road, air and train. Backup route prepared. Green corridor request created.
System / Dispatch
Transport page
IN_TRANSIT
7
Alerts sent to recipient hospital OT administration, assigned surgeon, transit team and traffic control. PDF report generated.
System
Alerts
ALERTED → TRANSPLANTED

5.2 Hold and repeat loop
If any step fails (no candidate, no viable route, doctor rejects), the case goes to HOLD with a clear reason such as 'No viable candidate within shelf life'. The screen shows 'No match found — case on hold'. The process then repeats automatically:
when a new donor or patient is added or updated;
when a hazard changes (strike ends, flood clears, traffic improves);
every 10 minutes by the scheduler for cases on hold (time is set in Admin settings);
until the organ shelf life ends. Then the donor organ becomes EXPIRED and everyone is notified.
Every attempt is saved (time, step that failed, reason) so the team can see the history.
5.3 Status lists (use these exact words in code and UI)
Object
Statuses
Patient
REGISTERED, ADMITTED, ACTIVE, MATCHED, IN_TRANSIT, TRANSPLANTED, INACTIVE, HOLD
Donor
AVAILABLE, MATCHING, ALLOCATED, ORGAN_IN_TRANSIT, COMPLETED, EXPIRED, HOLD
Match case
SEARCHING, PENDING_CONFIRMATION, CONFIRMED, REJECTED, IN_TRANSIT, DELIVERED, HOLD, CANCELLED
Green corridor
NOT_NEEDED, REQUESTED, VALIDATED, ACTIVE, COMPLETED, DENIED
Security code
ACTIVE, USED, EXPIRED, REVOKED

6. Authentication & Session Design (no JWT)
6.1 How login works
React sends POST /api/auth/login with email and password.
Spring Security checks the password (BCrypt), creates an HTTP session and sends a JSESSIONID cookie.
The cookie is HttpOnly (JavaScript cannot read it), Secure in production, SameSite=Lax.
The browser sends the cookie with every request. Axios must use withCredentials: true.
The login response returns the user profile (id, name, role, staffRole, hospitalId). React keeps it in AuthContext in memory only. Nothing secret is stored in localStorage.
On page refresh React calls GET /api/auth/me. If the session is alive it returns the user, otherwise 401 and the app goes to the login page.
Logout: POST /api/auth/logout invalidates the session and deletes the cookie.
6.2 Session rules
Setting
Value
Session timeout
30 minutes of inactivity. A popup warns at 28 minutes with a 'Stay signed in' button (calls /api/auth/ping).
Session fixation
New session ID after login (Spring default, keep it on)
Parallel sessions
Maximum 2 per user
Session store
Spring Session JDBC so sessions survive restarts
CSRF
Enabled. Spring CookieCsrfTokenRepository sends XSRF-TOKEN cookie; Axios sends it back in header X-XSRF-TOKEN.
CORS
Allow only the React origin with allowCredentials = true. In development use the Vite proxy so CORS is not needed.
Passwords
BCrypt, minimum 8 characters with a letter and a number
Lockout
5 wrong attempts → locked for 15 minutes. Both success and failure are written to the audit log.
401 response
React redirects to /login with the message 'Your session expired. Please sign in again.'
403 response
React shows the Access Denied page

6.3 Hospital security code flow
Admin creates the hospital record (Section 8, page A3).
Admin clicks Generate Security Code. The system creates a one-time code such as LR-7F3K-92QX linked to that hospital, valid for 72 hours.
The code is shown once on screen and also emailed to the hospital contact. The database stores only a hash of it.
The hospital opens /hospital/activate, enters the code and creates its Hospital Admin account (name, email, password).
The code becomes USED. The Hospital Admin then adds Coordinators, Doctors and Dispatch staff.
If a code is lost or leaked, Admin revokes it and generates a new one.
6.4 Patient and donor accounts
Patients and donors cannot self-register (prevents fake records). The hospital creates the record.
The system sends an invitation (SMS or email) with a one-time activation link valid for 48 hours. The person sets a password and signs in.
Deceased donors have no login. Their records are managed by the hospital only. Accounts are for living or pledged donors.
6.5 Route protection
Backend (real security): URL rules plus @PreAuthorize on services, for example hasRole('ADMIN') on /api/admin/**. Hospital APIs also check that the record belongs to the user's hospitalId.
Frontend (comfort only): a ProtectedRoute component hides pages the role cannot use and redirects to the right dashboard after login (Admin → /admin, Hospital → /hospital, Patient/Donor → /me).
7. Global Design System
Owner: Developer 1. All tokens live in frontend/src/styles/tokens.css as CSS variables (for example --color-primary). Nobody writes a hex color or pixel size directly inside a component. Everyone uses the tokens and the shared components from components/ui.
7.1 Design principles
Calm and trustworthy. Medical product, so clean white space, soft colors, no decoration that distracts.
Emergency is red, and only emergency is red. Red is reserved for critical alerts, 5-star urgency and dangerous actions.
One main action per screen. Only one Primary button is visible in each area.
Plain words. Use simple language. Show a tooltip for medical terms (HLA, PRA, ischemia).
Always show what happens next. Every screen tells the user the current status and the next step.
7.2 Color palette
Token
Name
Hex
Where to use
--color-primary
Root Teal
#0F766E
Primary buttons, links, active menu item, focus
--color-primary-dark
Deep Teal
#115E59
Primary button hover
--color-primary-light
Mint
#CCFBF1
Selected rows, highlight background
--color-secondary
Deep Navy
#0B2545
Sidebar, headings, table titles
--color-accent
Life Red
#E11D48
Logo heart, emergency button, 5-star, critical banner
--color-success
Green
#16A34A
Success messages, confirmed, viable
--color-warning
Amber
#F59E0B
Hold, warnings, medium risk
--color-danger
Red
#DC2626
Errors, delete, rejected, not viable
--color-info
Blue
#2563EB
Information, in transit, vehicle on map
--color-bg
Cloud
#F5F8FA
Page background
--color-surface
White
#FFFFFF
Cards, tables, modals
--color-border
Mist
#D9E2EC
Borders, dividers
--color-text
Ink
#1F2937
Main text
--color-text-muted
Slate
#64748B
Helper text, captions
--color-disabled
Fog
#CBD5E1
Disabled controls

Status badge colors (use the same everywhere)
Status
Badge color
Text
ACTIVE, AVAILABLE, CONFIRMED, VALIDATED
Green background #DCFCE7
#166534
HOLD, PENDING_CONFIRMATION, REQUESTED
Amber background #FEF3C7
#92400E
MATCHED, ALLOCATED, SEARCHING
Mint background #CCFBF1
#115E59
IN_TRANSIT, ORGAN_IN_TRANSIT
Blue background #DBEAFE
#1E40AF
TRANSPLANTED, COMPLETED, DELIVERED
Dark green background #166534
White
REJECTED, EXPIRED, DENIED, REVOKED
Red background #FEE2E2
#991B1B
INACTIVE, CANCELLED, NOT_NEEDED
Grey background #E2E8F0
#475569

AI star rating colors
Rating
Tier
Color
5 star
1st priority
Red #DC2626 on #FEE2E2
4 star
2nd priority
Orange #EA580C on #FFEDD5
3 star
3rd priority
Amber #CA8A04 on #FEF9C3

7.3 Typography
Font: Inter (Google Fonts) with fallback system-ui, Segoe UI, Arial, sans-serif. One font family for the whole site.
Style
Size / line height
Weight
Use
H1
28px / 36px
700
Page title
H2
22px / 30px
600
Section title
H3
18px / 26px
600
Card title
Body
16px / 24px
400
Normal text (never smaller on mobile)
Small
14px / 20px
400
Table text, helper text
Caption
12px / 16px
500
Labels, timestamps
Button
15px / 20px
600
All buttons

7.4 Spacing, shape and layout tokens
Item
Value
Spacing scale
4, 8, 12, 16, 24, 32, 48 px (use only these)
Radius
Buttons and inputs 8px, cards 12px, modals 16px, badges 999px (pill)
Card shadow
0 1px 3px rgba(11,37,69,0.08)
Modal shadow
0 10px 30px rgba(11,37,69,0.18)
Breakpoints
Mobile below 640px, Tablet 640–1024px, Desktop above 1024px
Max content width
1280px, centered
Icons
Lucide React, 20px inside text and buttons, 24px in menus

7.5 Button styles
All buttons: radius 8px, font 15px / 600, icon (optional) on the left with 8px gap, minimum touch height 40px.
Variant
Background
Text
Border
Hover
Use for
Primary
#0F766E
White
none
#115E59
Main action: Save, Login, Run Matching, Add Patient
Secondary
White
#0F766E
1.5px #0F766E
Background #F0FDFA
Cancel, Back, Export, second choice
Success
#16A34A
White
none
#15803D
Confirm Match, Mark Delivered
Danger
#DC2626
White
none
#B91C1C
Delete, Revoke Code, Reject Match
Emergency
#E11D48
White
none
#BE123C
Only: Activate Green Corridor, Emergency Re-match
Ghost (text)
Transparent
#0F766E
none
Background #F0FDFA
Table row actions, View details, links
Disabled
#CBD5E1
#64748B
none
no change, cursor not-allowed
Any unavailable action


Size
Height
Horizontal padding
Font
Use
Small
32px
12px
14px
Inside tables and cards
Medium (default)
40px
16px
15px
Most places
Large
48px
24px
16px
Login, landing page, main button on mobile

Button behavior rules
Loading: show a spinner instead of the label, keep the same width, disable the button (stops double click).
Focus: 3px ring in #99F6E4 with 2px offset. Never remove the focus ring.
Confirmation: every Danger and Emergency button opens a confirmation modal that states what will happen. Modal buttons: Cancel (Secondary) on the left, the action on the right.
Order: in forms and modals the Primary button is on the right; Cancel/Back on the left. On mobile buttons are full width and stacked, Primary on top.
Wording: start with a verb ('Add patient', 'Generate code'). Never write only 'Submit' or 'OK'.
7.6 Shared components (build once in components/ui)
Component
Specification
Input / Textarea
Height 44px, border 1px #D9E2EC, radius 8px. Label above (14px, 600), helper text below (12px, muted). Focus: border #0F766E + soft ring. Error: red border, red message with icon below. Required fields marked with *. Units shown inside the right edge (kg, cm, %).
Select, Date picker, Radio, Checkbox, Toggle
Same height and focus style as Input. Searchable select for long lists (hospital, organ).
Stepper
For long forms. Shows step number, step name and a progress line. Shows tick for completed steps.
Card
White, 12px radius, 1px border, 24px padding, optional header with title and action button.
Stat card
Big number (28px), label, small trend or icon. Used on dashboards.
Table
Header background #F1F5F9, row hover #F8FAFC, sticky header, sortable columns, search and filter bar above, pagination (10/25/50). On mobile the table turns into stacked cards.
Badge
Pill shape, colors from 7.2. Icon plus text, so color is never the only signal.
Modal
Max width 520px, dark overlay rgba(11,37,69,0.5), closes with ESC and X button, focus stays inside.
Toast
Top right. Success green disappears after 4 seconds. Error red stays until closed.
Alert banner
Full width inside page, left border 4px, icon, title, text. Types: info, success, warning, danger. Emergency banners use Life Red.
Skeleton
Grey animated blocks shaped like the final content. Use instead of spinners for pages.
Empty state
Simple icon, one sentence, and one button ('No patients yet — Add the first patient').
Tooltip
Small 'i' icon next to medical terms. Opens on hover, tap and keyboard focus.
Tabs and Breadcrumbs
Tabs for sections inside one record. Breadcrumbs on every detail page.
Timer / progress bar
Ischemia countdown bar: green when more than 50% time is left, amber 20–50%, red below 20%. Shows remaining time as text (2h 15m left).
Score bar
Horizontal stacked bar showing contribution of BloodMatch, Medical Health, Distance and Urgency to the total score. Same 4 colors everywhere: Teal, Blue, Amber, Red.
Map pins
Donor hospital = red heart pin, recipient hospital = teal hospital pin, vehicle = blue arrow, hazard = amber triangle.

7.7 Page layout
Public pages (landing, login, activation): top navbar 64px with logo on the left and links on the right (Home, How it works, Login). Footer with contact and a short medical disclaimer.
Application pages (Admin, Hospital, Patient/Donor):
Left sidebar 260px, Deep Navy background, white text, active item in Root Teal. Menu items depend on the role. On tablet it shrinks to an icon bar, on mobile it becomes a hamburger drawer.
Top bar 64px, white: page title, global search (Admin and Hospital only), notification bell with unread count, user menu (My profile, Change password, Logout).
Content area on Cloud background with 24px padding. Every page starts with a page header: title and one-line subtitle on the left, the page's Primary button on the right.
Emergency strip: if any case needs urgent action, a red alert banner appears just under the top bar on every page of that user.
Logo: text 'Life Root AI' with a heart-and-root icon. Heart in Life Red, text in Deep Navy (on dark backgrounds, white).
7.8 User-friendly rules (apply to every page)
Three states for every page: loading (skeleton), empty (message and button), error (message and Retry button).
Forms: labels above fields, validation when the user leaves a field, a summary of errors at the top if Save fails, save draft on long forms, never delete typed data on error.
Plain language and tooltips for medical words. Show units next to numbers.
Confirm every destructive or critical action. Toast after every save.
Prevent double submit: disable the button while the request is running.
Show who and when: 'Updated 10 min ago by Dr. Rao' on records.
Accessible: keyboard friendly, WCAG AA color contrast, aria-labels on icon buttons, color never the only signal.
Mobile first: every page must work at 360px width.
Formats: dates as 25 Oct 2026, time in 24-hour format with IST, distance in km, time in minutes or hours and minutes.
Smart defaults: pick address on a map and auto-fill latitude/longitude; remember last used filters; search inside long dropdowns.
Help: a 'What is this?' link on complex screens (matching score, viability). First-login short tour (can be skipped).
Safety first: emergency information stays at the top of the page and uses Life Red.
8. Page-by-Page Specification
Every page below lists its route, who can open it, what is on it, and which buttons it has. Button names follow the styles in Section 7.5. Every page also needs the three states from Section 7.8 (loading, empty, error).
Group
Page IDs
Owner
Public
P1–P6
Developer 1
Admin
A1–A12
Developer 1
Hospital
H1–H13
Developer 2 (H1–H7, H12, H13) and Developer 3 (H8–H11)
Patient / Donor
U1–U5
Developer 1
Shared
S1–S3
Developer 1

8.1 Public pages
P1 · Landing Page
Route: / · Access: Public
Hero: headline 'Right organ. Right patient. Right on time.', one-line explanation, illustration.
How it works: 4 cards — Register patient → AI matching → Best route → Instant alert.
Key features: 6 cards (AI matching, Why-matched explanation, Viability check, Multi-mode route, Green corridor, Instant alerts).
Who uses it: 3 cards (Admin, Hospital, Patient/Donor) with short text.
Footer: contact, privacy note, and the disclaimer 'Life Root AI supports doctors. Final medical decisions are made by the transplant team.'
Buttons: Login (Primary, Large), How it works (Secondary, Large, scrolls to section).
P2 · Login
Route: /login · Access: Public
Centered card: logo, Email, Password (with show/hide eye icon).
Error area for wrong password, locked account ('Too many attempts. Try again in 15 minutes') and expired session message.
Link: 'Forgot password?' and 'First time hospital? Activate with security code'.
Buttons: Sign in (Primary, Large, full width).
After success the user goes to /admin, /hospital or /me depending on role.
P3 · Hospital Activation
Route: /hospital/activate · Access: Public
Step 1 — Security code: input for the code (auto uppercase), hint 'Your admin sent this code by email'. Button: Verify code. On success shows the hospital name and city for confirmation.
Step 2 — Create account: full name, email, phone, password, confirm password, checkbox to accept terms. Live password rules checklist.
Buttons: Verify code (Primary), Activate hospital (Primary), Back (Secondary).
Errors: code invalid, expired, already used, revoked — each with its own clear message.
P4 · Patient / Donor Account Activation
Route: /activate/:token · Access: Public (invitation link)
Shows greeting with the person's name and hospital, set password and confirm password, password rules checklist.
Buttons: Set password and sign in (Primary, Large). Expired link shows 'Link expired — ask your hospital to send a new invitation'.
P5 · Forgot / Reset Password
Route: /forgot-password and /reset-password/:token · Access: Public
Enter email → message 'If this email exists, we sent a link' (do not reveal whether the email exists). Reset page: new password and confirm.
Buttons: Send reset link (Primary), Save new password (Primary), Back to login (Ghost).
P6 · Error pages
Route: /403, /404, /500 · Access: Public
Friendly icon, short message, one button: Go to my dashboard (Primary) or Go to home.
8.2 Admin pages (menu: Dashboard, Hospitals, Security Codes, Admins, Patients, Donors, Matches, Settings, Transport Data, Reports, Audit Log)
A1 · Admin Dashboard
Route: /admin · Access: ADMIN
Stat cards (6): Hospitals, Active patients, Available donors, Matches today, Cases on hold, Active transports.
Emergency strip: shown if any organ is close to expiry or a case is on hold for a 5-star patient.
Charts: matches per week (bar), average donor-to-recipient time (line), organs by type (bar).
Tables: 'Cases on hold' (latest 5), 'Recent activity' feed.
Buttons: Add hospital (Primary), Generate security code (Secondary), Add admin (Secondary).
A2 · Hospitals List
Route: /admin/hospitals · Access: ADMIN
Search box and filters (status, city, state, organs transplanted).
Table columns: Hospital name, City, Registration no., Contact person, Status badge, Staff, Patients, Donors, Actions.
Row actions (Ghost): View, Edit, Generate code, Suspend.
Buttons: Add hospital (Primary).
A3 · Add / Edit Hospital
Route: /admin/hospitals/new and /admin/hospitals/:id/edit · Access: ADMIN
Basic details: name, registration or licence number, type (Government, Private, Trust), organs the hospital is licensed for (multi-select).
Contact: contact person, phone, email, 24x7 emergency number.
Location: address, city, state, pincode, map picker (fills latitude and longitude).
Capabilities: helipad (yes/no), trauma center (yes/no), number of operation theaters, nearest airport and railway station.
Buttons: Save hospital (Primary), Save and generate code (Secondary), Cancel (Secondary).
A4 · Hospital Detail
Route: /admin/hospitals/:id · Access: ADMIN
Header with name, status badge and quick facts. Tabs: Overview, Staff, Patients, Donors, Matches, Security codes, Activity.
Buttons: Edit (Secondary), Generate code (Secondary), Suspend hospital (Danger, with confirmation; suspended staff cannot log in).
A5 · Security Codes
Route: /admin/security-codes · Access: ADMIN
Table: Code (masked, for example LR-****-92QX), Hospital, Created by, Created at, Expires at, Status badge (ACTIVE, USED, EXPIRED, REVOKED), Actions.
Generate code modal: choose hospital, validity (24 hours, 72 hours, 7 days), checkbox 'Email the code to hospital contact'.
After generation the full code is shown once in a large box with a Copy button and the warning 'This code will not be shown again'.
Buttons: Generate code (Primary), Copy code (Secondary), Revoke (Danger, row action).
A6 · Admin Users
Route: /admin/admins · Access: ADMIN
Table of all admins: name, email, phone, last login, status.
Add admin form (name, email, phone). A temporary password is emailed and the admin must change it at first login.
Rules: an admin cannot deactivate themselves, and the last active admin can never be deactivated.
Buttons: Add admin (Primary), Reset password (Secondary), Deactivate (Danger).
A7 · All Patients and All Donors
Route: /admin/patients and /admin/donors · Access: ADMIN
Global lists with filters: hospital, organ, blood group, star rating, status.
Row click opens the same detail page as the hospital view (H4, H7), with full edit rights for admin.
Buttons: Export CSV (Secondary).
A8 · Matches Monitor
Route: /admin/matches · Access: ADMIN
Table: Case ID, Donor hospital, Patient hospital, Organ, Score, Status badge, ETA, Ischemia time left (timer bar).
Filters: status, organ, hospital, date.
Row click opens the shared Match Detail page (H8) with admin rights.
Buttons (inside detail): Emergency re-match (Emergency, with confirmation), Cancel case (Danger).
A9 · Algorithm and System Settings
Route: /admin/settings · Access: ADMIN
Tab 1 — Matching weights: sliders for a1 (blood), a2 (health), a3 (distance), a4 (urgency). Total must equal 1.00 (shows live total and blocks save if not). Wait-time rule: after 365 days add +0.10 to urgency weight.
Tab 2 — Urgency and stars: score ranges for 5, 4 and 3 star; wait days to promote 3→4 and 4→5 star.
Tab 3 — Organ shelf life: table of organ, safe hours, handover buffer minutes.
Tab 4 — Transport: air transport minimum distance (default 50 km), traffic penalties (LOW 0, MODERATE +15, SEVERE +40 minutes), maximum candidates to check.
Tab 5 — Retry and alerts: hold retry interval, alert message templates for SMS and email.
Tab 6 — Civic points: points for registration, camp attendance, referral.
A yellow note on top: 'Changes affect all hospitals. Medical advisor approval recommended.' Every change stores who changed it, old value and new value.
Buttons: Save changes (Primary), Reset to default (Secondary, with confirmation).
A10 · Audit Log
Route: /admin/audit · Access: ADMIN (read only)
Filters: user, hospital, action type (LOGIN, CREATE, UPDATE, MATCH, VIEW_PATIENT, SETTINGS), date range.
Table: Time, User, Role, Hospital, Action, Object, IP address. Records cannot be edited or deleted.
Buttons: Export CSV (Secondary).
A11 · Reports and Analytics
Route: /admin/reports · Access: ADMIN
Date range filter. Cards: total matches, success rate, average donor-to-recipient time, organs expired.
Charts: matches by organ, hold reasons, transport mode used.
Table of generated match PDFs with Download buttons.
Buttons: Download PDF (Secondary), Export summary CSV (Secondary).
A12 · Transport Data (added)
Route: /admin/transport-data · Access: ADMIN
Live railway and police systems are not freely available, so version 1 uses data maintained here.
Tab Trains and metro: corridor name, from, to, stations with coordinates, running hours, status (available / not available today).
Tab Air: airports, hospital helipads, air-ambulance charter contacts.
Tab Police and green corridor contacts: city, control room phone and email.
Tab Events and hazards: festivals, rallies, bandh/strike, flood alerts, each with date range, area and affected roads. Hospitals and dispatch teams can also report an event; admin approves it.
Buttons: Add entry (Primary), Edit (Ghost), Delete (Danger).
