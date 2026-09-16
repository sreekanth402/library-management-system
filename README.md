# Library Management System

Full-stack library administration app: **Spring Boot 3 + JWT + MySQL** on the backend and **React + TypeScript + Vite** on the frontend.

## Run after cloning from GitHub (recommended)

GitHub stores the code. After you clone the repo, Docker Compose starts the UI, API, and MySQL together:

```bash
git clone https://github.com/sreekanth402/library-management-system.git
cd library-management-system
docker compose up --build
```

Then open:

- App: http://localhost:3000
- API / Swagger: http://localhost:8081/swagger-ui.html
- MySQL: localhost:3306

Stop with `Ctrl+C`, or `docker compose down`.

### Demo accounts

| Username   | Password       | Role      |
|------------|----------------|-----------|
| admin      | Admin@123      | ADMIN     |
| librarian  | Librarian@123  | LIBRARIAN |
| member     | Member@123     | MEMBER    |

Fines are **₹10 per late day**. Loan period is **14 days**.

CI runs on every push to `main` (backend tests + frontend build).

## Local development (without Docker)

You need:

- Java 21+ (Java 24 works; the project compiles to Java 21)
- Maven 3.9+ **or** the Maven wrapper in `backend/`
- Node.js 22+

Local development defaults to an **H2 file database** so the app starts without MySQL. Use the `dev` profile when you want MySQL.

The API listens on **8081** (8080 is often already in use).

### 1. Run the backend

```bash
cd backend
mvn spring-boot:run
```

Or with the Maven wrapper (no Maven install required): `.\mvnw.cmd spring-boot:run` (Windows) / `./mvnw spring-boot:run` (macOS/Linux).

API: http://localhost:8081  
Swagger: http://localhost:8081/swagger-ui.html  
H2 console: http://localhost:8081/h2-console (JDBC URL `jdbc:h2:file:./data/library`)

To use MySQL instead:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 2. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

UI: http://localhost:5173 (Vite proxies `/api` to port 8081)

## What this version includes

- Layered backend: Controller → Service → Repository → JPA → MySQL
- Book, member, borrow/return, and fine calculation business logic
- Spring Security + JWT with `ADMIN`, `LIBRARIAN`, and `MEMBER` roles
- Pagination and search for books
- Global exception handling and Bean Validation
- Swagger UI at `/swagger-ui.html`
- React dashboard with login, books, members, issue, returns, and transactions
- Docker Compose for frontend, backend, and MySQL
- GitHub Actions CI (backend tests + frontend build)

## REST overview

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET/POST/PUT/DELETE /api/books`
- `GET/POST/PUT/DELETE /api/members`
- `POST /api/borrow`
- `POST /api/return/{borrowId}`
- `GET /api/borrow/active`
- `GET /api/borrow/history`
- `GET /api/borrow/me`
- `GET /api/dashboard/stats`

## Project layout

```text
library-management-system/
├── backend/     Spring Boot API (port 8081)
├── frontend/    React + TypeScript UI
├── database/    MySQL init script
├── docker/      Extra Docker/Nginx files
├── docker-compose.yml
└── .github/workflows/ci.yml
```
