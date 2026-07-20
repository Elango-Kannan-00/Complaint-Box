# Resolvr — Campus Complaint Management (Frontend)

Modern React + TanStack Start frontend for the Spring Boot Complaint Management System.

## Setup

```bash
bun install    # or npm install
bun dev        # starts on http://localhost:3000
```

Point at your backend via `.env`:

```
VITE_API_URL=http://localhost:8080
```

Frontend dev server runs on `http://localhost:3000` and the backend stays on `http://localhost:8080`.

## Features

- Landing / login page (email, password, role, user ID)
- Landing / login page (email and password only; backend returns the user profile)
- Student registration with department picker
- Student dashboard: stats, filters, create/edit/delete complaints, feedback
- HOD dashboard: review, refresh, one-click status progression with confirm dialogs
- Toast notifications (sonner), confirm dialogs, hover effects, animations

## Backend

Runs against the provided Spring Boot API. Endpoints:
`/user/*`, `/departments/*`, `/complaints/*`, `/hod/*`.
