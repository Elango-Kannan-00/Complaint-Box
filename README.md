# Complaint-Box

Campus complaint management system with a React frontend and a Spring Boot backend.

The repository is split into two main parts:

- `frontend/` - TanStack Start + React UI
- `backend/` - Spring Boot REST API + PostgreSQL persistence

## What the project does

ResolveR lets students register, log in, raise complaints, track complaint progress, and submit feedback after resolution. HOD users can review assigned complaints and move them through the workflow.

## Project Structure

```text
Complaint-Box/
├── frontend/
│   ├── src/
│   │   ├── routes/            # Pages: login, register, student dashboard, HOD dashboard
│   │   ├── components/        # Shared UI building blocks
│   │   ├── lib/               # API client, auth session helpers, utilities
│   │   └── styles.css         # Global styling
│   ├── public/                # Static assets
│   └── package.json
├── backend/
│   ├── src/main/java/com/cms/backend/
│   │   ├── controller/        # REST controllers
│   │   ├── service/           # Business logic
│   │   ├── repository/        # Spring Data JPA repositories
│   │   ├── entity/            # JPA entities
│   │   ├── dto/               # Request/response DTOs
│   │   ├── enums/             # Complaint, role, and department enums
│   │   └── config/            # Security, CORS, password config
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── data.sql
│   └── pom.xml
└── README.md
```

## Tech Stack

### Frontend

- React 19
- TanStack Start
- TanStack Router
- TanStack Query
- TypeScript
- Vite
- Tailwind CSS 4
- shadcn/ui + Radix UI components

### Backend

- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Security
- Validation
- PostgreSQL
- Lombok

## Features

### Student Experience

- Register with full name, email, password, and academic department
- Sign in with email and password
- View a student dashboard with complaint stats
- Create new complaints
- Edit complaint title and description
- Delete complaints when allowed by business rules
- Filter complaints by status
- Submit feedback for resolved complaints
- View account/profile details

### HOD Experience

- View complaints assigned to the HOD's department
- Review complaint summaries and student details
- Update complaint status
- Filter complaints by status
- Refresh the complaint queue

### Platform Behavior

- Session is stored client-side for quick dashboard navigation
- API requests are centralized in the frontend client
- Backend uses DTOs for request/response shaping
- Passwords are handled through Spring Security password encoding

## Complaint Workflow

The backend currently supports these complaint states:

- `PENDING`
- `IN_PROGRESS`
- `RESOLVED`

User roles currently defined in the backend:

- `STUDENT`
- `HOD`
- `PRINCIPAL`
- `EXECUTIVE_CHAIRMAN`

## API Endpoints

Base URL: `http://localhost:8080`

### Auth

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/user/register` | Register a new user |
| `POST` | `/user/login` | Authenticate a user |
| `GET` | `/user/profile?email={email}` | Fetch a public user profile by email |

#### Register payload

```json
{
  "userName": "Alex Johnson",
  "userEmail": "alex@campus.edu",
  "userPassword": "password123",
  "academicDepartmentId": 1
}
```

#### Login payload

```json
{
  "userEmail": "alex@campus.edu",
  "userPassword": "password123"
}
```

### Departments

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/departments/academic-departments` | List all academic departments |
| `GET` | `/departments/complaint-departments/{studentId}` | List complaint departments available to a student |

### Complaints

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/complaints/{studentId}` | Create a complaint for a student |
| `GET` | `/complaints/{studentId}` | Get all complaints for a student |
| `PUT` | `/complaints/{complaintId}` | Update complaint title and description |
| `DELETE` | `/complaints/{complaintId}` | Delete a complaint |
| `PUT` | `/complaints/{complaintId}/feedback` | Submit feedback for a resolved complaint |

#### Create complaint payload

```json
{
  "complaintTitle": "Projector not working",
  "complaintDescription": "The projector in room 204 is not turning on.",
  "complaintDepartmentId": 3
}
```

#### Update complaint payload

```json
{
  "complaintTitle": "Updated title",
  "complaintDescription": "Updated description"
}
```

#### Feedback payload

```json
{
  "feedback": "Resolved quickly. Thanks!"
}
```

### HOD

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/hod/{hodId}/complaints` | Get complaints assigned to a HOD |
| `PUT` | `/hod/complaints/{complaintId}/status` | Update complaint status |

#### Status update payload

```json
{
  "complaintStatus": "IN_PROGRESS"
}
```

## API Response Shapes

### User

```json
{
  "userId": 1,
  "userName": "Alex Johnson",
  "userEmail": "alex@campus.edu",
  "userRole": "STUDENT"
}
```

### Complaint

```json
{
  "complaintId": 12,
  "complaintTitle": "Projector not working",
  "complaintDescription": "The projector in room 204 is not turning on.",
  "complaintStatus": "PENDING",
  "departmentName": "Electrical",
  "createdAt": "2026-07-21T10:00:00.000+00:00",
  "updatedAt": "2026-07-21T10:15:00.000+00:00",
  "feedback": null
}
```

### HOD Complaint

```json
{
  "complaintId": 12,
  "complaintTitle": "Projector not working",
  "complaintDescription": "The projector in room 204 is not turning on.",
  "studentName": "Alex Johnson",
  "complaintStatus": "PENDING",
  "departmentName": "Electrical",
  "createdAt": "2026-07-21T10:00:00.000+00:00"
}
```

## Configuration

### Backend

The backend uses PostgreSQL and reads its connection from:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cms_db
spring.datasource.username=postgres
spring.datasource.password=
```

JPA is configured with:

- `spring.jpa.hibernate.ddl-auto=update`
- SQL logging enabled

### Frontend

The frontend points to the API through `VITE_API_URL`.

```bash
VITE_API_URL=http://localhost:3000
```

If `VITE_API_URL` is not set, the frontend defaults to `http://localhost:8080`.

## Local Development

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

The backend will run on `http://localhost:8080`.

### Frontend

```bash
cd frontend
bun install
bun dev
```

The frontend will run on `http://localhost:3000`.

If you use npm instead of Bun:

```bash
npm install
npm run dev
```

## Notes

- The frontend currently focuses on student and HOD flows.
- `PRINCIPAL` and `EXECUTIVE_CHAIRMAN` exist in the backend role enum, but there are no dedicated frontend routes for them yet.
- The backend and frontend are intentionally separated so each can be developed and deployed independently.
