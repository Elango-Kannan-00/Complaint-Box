# ResolveR Backend

Spring Boot REST API for the Complaint-Box / ResolveR application.

This backend handles authentication, department lookup, complaint lifecycle operations, and HOD complaint management. It is designed to work with the TanStack Start frontend in this repository.

## Highlights

- User registration and login
- Public profile lookup by email
- Academic department list API
- Complaint department lookup for student complaint creation
- Complaint create, update, delete, and feedback flow
- HOD complaint queue and status updates
- CORS configured for the deployed Vercel frontend and local development
- Database seeding for departments

## Tech Stack

- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Security
- Validation
- PostgreSQL
- Lombok

## Project Structure

```text
backend/
├── mvnw, mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/cms/backend/
│   │   │   ├── config/      # CORS, security, password config, data seeding
│   │   │   ├── controller/  # REST controllers
│   │   │   ├── dto/         # Request and response DTOs
│   │   │   ├── entity/      # JPA entities
│   │   │   ├── enums/       # Complaint, role, and department enums
│   │   │   ├── repository/  # Spring Data JPA repositories
│   │   │   └── service/     # Business logic
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
└── README.md
```

## Domain Model

### User roles

- `STUDENT`
- `HOD`
- `PRINCIPAL`
- `EXECUTIVE_CHAIRMAN`

### Complaint statuses

- `PENDING`
- `IN_PROGRESS`
- `RESOLVED`

### Department types

- `COMMON`
- `ACADEMIC`

## Configuration

### Database

The backend expects PostgreSQL connection values from environment variables:

```properties
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

If those are not provided locally, the sample `application.properties` can be adjusted for a local database.

### JPA / SQL init

The backend is configured to:

- defer datasource initialization
- always run SQL initialization
- seed departments on startup through a `CommandLineRunner`

This helps keep the academic and complaint department tables populated on Render.

### CORS

The backend allows requests from:

- `https://resolve-r-frontend.vercel.app`
- Vercel preview domains matching `https://*.vercel.app`
- `http://localhost:3000`
- `http://127.0.0.1:3000`

## API Endpoints

Base URL:

```text
https://resolver-backend.onrender.com
```

### Auth

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/user/register` | Register a new student user |
| `POST` | `/user/login` | Authenticate a user |
| `GET` | `/user/profile?email={email}` | Fetch a public profile by email |

#### Register body

```json
{
  "userName": "Alex Johnson",
  "userEmail": "alex@campus.edu",
  "userPassword": "password123",
  "academicDepartmentId": 1
}
```

#### Login body

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
| `GET` | `/departments/complaint-departments/{studentId}` | List complaint departments for a student |

### Complaints

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/complaints/{studentId}` | Create a complaint |
| `GET` | `/complaints/{studentId}` | Get all complaints for a student |
| `PUT` | `/complaints/{complaintId}` | Update complaint title and description |
| `DELETE` | `/complaints/{complaintId}` | Delete a complaint |
| `PUT` | `/complaints/{complaintId}/feedback` | Submit feedback for a resolved complaint |

#### Create complaint body

```json
{
  "complaintTitle": "Projector not working",
  "complaintDescription": "The projector in room 204 is not turning on.",
  "complaintDepartmentId": 3
}
```

#### Update complaint body

```json
{
  "complaintTitle": "Updated title",
  "complaintDescription": "Updated description"
}
```

#### Feedback body

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

#### Status update body

```json
{
  "complaintStatus": "IN_PROGRESS"
}
```

## Response Shapes

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

## Local Development

Run the backend:

```bash
./mvnw spring-boot:run
```

The API runs on `http://localhost:8080` in development.

## Build and Test

Compile:

```bash
./mvnw -DskipTests compile
```

Run tests:

```bash
./mvnw test
```

Package:

```bash
./mvnw clean package
```

## Seed Data

The application seeds:

- academic departments
- complaint departments

This ensures the frontend can render department dropdowns for both registration and complaint creation.

## Notes

- The backend currently uses `RuntimeException` for many business-rule failures.
- Some invalid inputs may return `500` until a global exception handler is added.
- Frontend deployment should point `VITE_API_URL` to the Render backend URL.
