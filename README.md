# Password & Notes Vault

Secure full-stack application to store passwords and notes with AES-256-GCM encryption and JWT authentication.

## Tech Stack
- Backend: Spring Boot 3, Java 21, Spring Security, Spring Data JPA, PostgreSQL/MySQL, JWT, BCrypt
- Frontend: React 18 + Vite + TypeScript, Tailwind CSS, React Router, React Query, Zod

## Monorepo Structure
```
backend/
frontend/
.env.example
```

## Prerequisites
- Java 21, Maven
- Node.js 18+
- PostgreSQL or MySQL

## Environment Variables
Copy `.env.example` to `.env` and fill in values. The backend reads environment variables via Spring configuration. The frontend uses `VITE_` prefixed variables.

Important:
- Generate AES key: `openssl rand -base64 32` and set `AES_KEY_BASE64`
- Set a strong `JWT_SECRET` (>= 32 bytes)
- Set `CORS_ALLOWED_ORIGINS` to include your frontend URL(s)

## Backend: Run Locally
```
cd backend
mvn spring-boot:run
```
Backend starts at `http://localhost:8080`.

### Database
Default is Postgres via `DB_URL`. For MySQL:
- Set `DB_URL=jdbc:mysql://localhost:3306/vault?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
- Set `DB_DRIVER=com.mysql.cj.jdbc.Driver`
- Provide `DB_USERNAME` and `DB_PASSWORD`

### Key Endpoints
- POST `/api/auth/register` { email, password }
- POST `/api/auth/login` { email, password }
- GET `/api/user/me` (Authorization: Bearer <token>)
- CRUD `/api/vault`

## Frontend: Run Locally
```
cd frontend
npm install
npm run dev
```
Frontend at `http://localhost:5173`.

Set `VITE_API_URL` to your backend URL.

## CORS
The backend enables CORS and accepts origins from `CORS_ALLOWED_ORIGINS` (comma-separated). Ensure it includes the frontend URL(s).

- Backend reads `security.cors.allowed-origins` (from `application.yml`) or `CORS_ALLOWED_ORIGINS` env. Example: `http://localhost:5173,https://your-frontend.app`.
- Credentials: Allowed; `Authorization` header is permitted. Use Bearer tokens in requests from the frontend.
- Frontend: Set `VITE_API_URL` to the backend origin; React Query/axios attach `Authorization: Bearer <token>`.

Example axios usage is pre-configured in `frontend/src/shared/api.ts`.

## Security Notes
- Passwords stored using BCrypt
- Vault data encrypted at rest with AES-256-GCM; server-side encryption/decryption
- JWT stateless authentication with HS256
- Validation via `jakarta.validation` annotations; structured errors

## Deployment
### Backend (Render)
1. Create a new Web Service, point to `/workspace/backend` repo directory.
2. Environment: `Docker` or `Build & Run with Maven`.
   - Build command: `mvn -DskipTests package`
   - Start command: `java -jar target/password-notes-vault-0.0.1-SNAPSHOT.jar`
3. Add env vars: `PORT`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_DRIVER`, `JWT_SECRET`, `AES_KEY_BASE64`, `CORS_ALLOWED_ORIGINS`.
4. Database: Add a managed Postgres, and set `DB_URL` to Render’s JDBC string.

### Backend (Railway)
1. Create a Spring app service, attach a Postgres plugin.
2. Set env vars from the plugin: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_DRIVER=org.postgresql.Driver`.
3. Set `JWT_SECRET`, `AES_KEY_BASE64` and `CORS_ALLOWED_ORIGINS` to your frontend domain.

### Frontend (Netlify)
1. Create a site from the `frontend/` directory.
2. Build command: `npm run build`, Publish directory: `dist`.
3. Environment variable: `VITE_API_URL=https://<your-backend-domain>`.

### Frontend (Vercel)
1. Import project, root set to `frontend`.
2. Build command auto-detected, Output directory: `dist`.
3. Add `VITE_API_URL` in Project Settings.

## Database Setup
PostgreSQL example:
```
createdb vault
export DB_URL=jdbc:postgresql://localhost:5432/vault
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export DB_DRIVER=org.postgresql.Driver
```

MySQL example:
```
CREATE DATABASE vault CHARACTER SET utf8mb4;
export DB_URL="jdbc:mysql://localhost:3306/vault?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export DB_USERNAME=root
export DB_PASSWORD=secret
export DB_DRIVER=com.mysql.cj.jdbc.Driver
```

Crypto keys:
```
export AES_KEY_BASE64=$(openssl rand -base64 32)
export JWT_SECRET="change-me-to-a-strong-secret-string-with-32+chars"
```

## License
MIT


