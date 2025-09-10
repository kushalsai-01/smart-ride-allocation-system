# Password and Notes Vault

A secure, full-stack web application for storing and managing passwords and notes with enterprise-grade encryption and modern UI/UX.

![Password Vault](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)
![React](https://img.shields.io/badge/React-18.2.0-blue.svg)
![TypeScript](https://img.shields.io/badge/TypeScript-5.2.2-blue.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)

## 🔒 Security Features

- **AES-256-GCM Encryption**: All sensitive data encrypted at rest
- **bcrypt Password Hashing**: Secure password storage with salt rounds
- **Session Management**: Secure HTTP sessions with Spring Security
- **CORS Protection**: Configurable cross-origin resource sharing
- **Input Validation**: Comprehensive server-side and client-side validation
- **SQL Injection Protection**: JPA/Hibernate query protection
- **XSS Protection**: Content Security Policy headers

## 🚀 Features

### Backend (Spring Boot)
- ✅ RESTful API with comprehensive endpoints
- ✅ PostgreSQL/MySQL database support
- ✅ JWT and Session-based authentication
- ✅ AES-256-GCM encryption for sensitive data
- ✅ bcrypt password hashing
- ✅ Spring Data JPA with entity relationships
- ✅ Global exception handling
- ✅ Request validation with meaningful error messages
- ✅ Configurable CORS support
- ✅ Health check endpoints
- ✅ Audit trails with created/updated timestamps

### Frontend (React + TypeScript)
- ✅ Modern React with TypeScript and hooks
- ✅ Tailwind CSS with dark/light theme support
- ✅ Responsive design for desktop and mobile
- ✅ React Router for client-side navigation
- ✅ React Query for server state management
- ✅ Form validation with react-hook-form and Zod
- ✅ Copy-to-clipboard functionality
- ✅ Toast notifications
- ✅ Loading states and error handling
- ✅ Password strength indicator
- ✅ Search and filter capabilities

### Vault Management
- ✅ Create, read, update, delete vault items
- ✅ Multiple item types (passwords, notes, credit cards, identity)
- ✅ Favorites system
- ✅ Categories and tags
- ✅ Search functionality
- ✅ Recently accessed items
- ✅ Vault statistics dashboard

## 🏗️ Architecture

```
password-vault/
├── backend/                 # Spring Boot API
│   ├── src/main/java/com/passwordvault/
│   │   ├── config/         # Security, CORS, JPA configuration
│   │   ├── controller/     # REST API endpoints
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── entity/        # JPA entities
│   │   ├── exception/     # Custom exceptions and handlers
│   │   ├── repository/    # Data access layer
│   │   ├── service/       # Business logic layer
│   │   └── util/          # Utility classes (encryption)
│   └── src/main/resources/
│       ├── application.yml # Main configuration
│       └── data.sql       # Sample data
├── frontend/               # React TypeScript app
│   ├── src/
│   │   ├── components/    # Reusable UI components
│   │   ├── hooks/         # Custom React hooks
│   │   ├── lib/           # Utilities and API client
│   │   ├── pages/         # Page components
│   │   ├── styles/        # Global styles and Tailwind
│   │   └── types/         # TypeScript type definitions
│   └── public/            # Static assets
└── docs/                  # Documentation
```

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL / MySQL
- **Security**: Spring Security 6
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Bean Validation (JSR-303)
- **Build Tool**: Maven
- **Session Store**: JDBC-based sessions

### Frontend
- **Framework**: React 18.2.0
- **Language**: TypeScript 5.2.2
- **Build Tool**: Vite 4.5.0
- **Styling**: Tailwind CSS 3.3.5
- **Routing**: React Router 6.20.1
- **State Management**: React Query 5.8.4
- **Forms**: React Hook Form 7.48.2
- **Validation**: Zod 3.22.4
- **UI Components**: Headless UI, Lucide React
- **Animations**: Framer Motion

## 📋 Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **PostgreSQL 13** or higher (or MySQL 8.0+)
- **Maven 3.6** or higher
- **Git**

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/password-vault.git
cd password-vault
```

### 2. Database Setup

#### PostgreSQL Setup
```bash
# Install PostgreSQL (Ubuntu/Debian)
sudo apt update
sudo apt install postgresql postgresql-contrib

# Create database and user
sudo -u postgres psql
CREATE DATABASE password_vault;
CREATE USER vault_user WITH ENCRYPTED PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE password_vault TO vault_user;
\q
```

#### MySQL Setup (Alternative)
```bash
# Install MySQL
sudo apt update
sudo apt install mysql-server

# Create database and user
sudo mysql
CREATE DATABASE password_vault;
CREATE USER 'vault_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON password_vault.* TO 'vault_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Backend Setup

```bash
cd backend

# Copy and configure environment variables
cp .env.example .env

# Edit .env with your database credentials
nano .env

# Build and run the application
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 4. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Copy and configure environment variables
cp .env.example .env.local

# Start development server
npm run dev
```

The frontend will start on `http://localhost:3000`

## ⚙️ Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/password_vault
    username: vault_user
    password: your_secure_password
    
app:
  encryption:
    secret-key: your-256-bit-secret-key-here-change-in-production
  cors:
    allowed-origins: http://localhost:3000,https://yourdomain.com
```

### Frontend Configuration

Edit `frontend/.env.local`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME="Password Vault"
```

## 🔐 Security Configuration

### Environment Variables

**Critical Security Settings:**

```bash
# Backend (.env)
ENCRYPTION_SECRET_KEY=your-256-bit-secret-key-32-characters-long
JWT_SECRET=your-jwt-secret-key-change-in-production
DATABASE_PASSWORD=your-secure-database-password
CORS_ALLOWED_ORIGINS=https://yourdomain.com

# Generate secure keys
openssl rand -base64 32  # For encryption key
openssl rand -base64 64  # For JWT secret
```

### Production Security Checklist

- [ ] Change all default passwords and secrets
- [ ] Use HTTPS in production
- [ ] Configure secure session cookies
- [ ] Set up proper CORS origins
- [ ] Enable SQL query logging monitoring
- [ ] Set up rate limiting
- [ ] Configure CSP headers
- [ ] Use environment-specific configurations
- [ ] Set up backup encryption keys
- [ ] Enable audit logging

## 🚀 Deployment

### Backend Deployment (Railway/Render)

#### Railway Deployment

1. **Prepare for Railway:**
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login

# Initialize project
railway init
```

2. **Configure Railway:**
```bash
# Set environment variables
railway variables set SPRING_PROFILES_ACTIVE=prod
railway variables set DATABASE_URL=your-production-db-url
railway variables set ENCRYPTION_SECRET_KEY=your-production-encryption-key
railway variables set JWT_SECRET=your-production-jwt-secret
railway variables set CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com

# Deploy
railway up
```

#### Render Deployment

1. **Create `render.yaml`:**
```yaml
services:
  - type: web
    name: password-vault-backend
    env: java
    buildCommand: cd backend && mvn clean install -DskipTests
    startCommand: cd backend && java -jar target/password-vault-backend-1.0.0.jar
    envVars:
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: DATABASE_URL
        fromDatabase:
          name: password-vault-db
          property: connectionString
      - key: ENCRYPTION_SECRET_KEY
        generateValue: true
      - key: JWT_SECRET
        generateValue: true

databases:
  - name: password-vault-db
    databaseName: password_vault
    user: vault_user
```

### Frontend Deployment (Netlify/Vercel)

#### Netlify Deployment

1. **Build Configuration:**
```bash
# Create netlify.toml
[build]
  command = "npm run build"
  publish = "dist"

[build.environment]
  VITE_API_BASE_URL = "https://your-backend-domain.com/api"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

2. **Deploy:**
```bash
# Install Netlify CLI
npm install -g netlify-cli

# Login and deploy
netlify login
netlify init
netlify deploy --prod
```

#### Vercel Deployment

1. **Configure `vercel.json`:**
```json
{
  "builds": [
    {
      "src": "package.json",
      "use": "@vercel/static-build",
      "config": {
        "distDir": "dist"
      }
    }
  ],
  "routes": [
    {
      "src": "/(.*)",
      "dest": "/index.html"
    }
  ]
}
```

2. **Deploy:**
```bash
# Install Vercel CLI
npm install -g vercel

# Deploy
vercel --prod
```

## 🧪 Testing

### Backend Testing

```bash
cd backend

# Run unit tests
mvn test

# Run integration tests
mvn integration-test

# Generate test coverage report
mvn jacoco:report
```

### Frontend Testing

```bash
cd frontend

# Run unit tests
npm test

# Run e2e tests
npm run test:e2e

# Generate coverage report
npm run test:coverage
```

## 📊 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/logout` | User logout |
| GET | `/api/auth/me` | Get current user |
| GET | `/api/auth/check-username` | Check username availability |
| GET | `/api/auth/check-email` | Check email availability |

### Vault Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vault/items` | Get all vault items |
| POST | `/api/vault/items` | Create vault item |
| GET | `/api/vault/items/{id}` | Get vault item by ID |
| PUT | `/api/vault/items/{id}` | Update vault item |
| DELETE | `/api/vault/items/{id}` | Delete vault item |
| PATCH | `/api/vault/items/{id}/favorite` | Toggle favorite status |
| GET | `/api/vault/items/favorites` | Get favorite items |
| GET | `/api/vault/items/search` | Search vault items |
| GET | `/api/vault/stats` | Get vault statistics |

### User Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/user/profile` | Get user profile |
| PUT | `/api/user/profile` | Update user profile |
| PUT | `/api/user/password` | Change password |
| GET | `/api/user/stats` | Get user statistics |
| DELETE | `/api/user/account` | Delete user account |

## 🐛 Troubleshooting

### Common Issues

#### Backend Issues

**Database Connection Failed**
```bash
# Check database status
sudo systemctl status postgresql

# Check connection
psql -h localhost -U vault_user -d password_vault

# Common fixes
- Verify database credentials in application.yml
- Ensure database server is running
- Check firewall settings
- Verify user permissions
```

**Encryption Errors**
```bash
# Verify encryption key length (must be 32 characters)
echo -n "your-key" | wc -c

# Generate new key
openssl rand -base64 32
```

#### Frontend Issues

**API Connection Failed**
```bash
# Check backend URL in .env.local
VITE_API_BASE_URL=http://localhost:8080/api

# Verify CORS configuration
# Check browser developer console for CORS errors
```

**Build Errors**
```bash
# Clear node modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Clear build cache
npm run build -- --force
```

### Performance Optimization

#### Backend Optimization

```yaml
# application-prod.yml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
  
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

#### Frontend Optimization

```javascript
// Lazy loading routes
const DashboardPage = lazy(() => import('@/pages/dashboard/DashboardPage'));

// Image optimization
<img loading="lazy" src="..." alt="..." />

// Bundle analysis
npm run build -- --analyze
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding standards (Google Style Guide)
- Use TypeScript strict mode
- Write unit tests for new features
- Update documentation for API changes
- Follow semantic versioning

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) - Backend framework
- [React](https://reactjs.org/) - Frontend library
- [Tailwind CSS](https://tailwindcss.com/) - CSS framework
- [Lucide](https://lucide.dev/) - Icon library
- [React Query](https://tanstack.com/query) - Server state management

## 📞 Support

For support, email support@passwordvault.com or create an issue in the GitHub repository.

---

**⚠️ Security Notice**: This application handles sensitive data. Always use HTTPS in production, regularly update dependencies, and follow security best practices. Never commit secrets to version control.