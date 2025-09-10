# Local Development Setup Guide

This guide will help you set up the Password Vault application for local development.

## 📋 Prerequisites

Before starting, ensure you have the following installed:

- **Java 17 or higher** - [Download](https://adoptium.net/)
- **Node.js 18 or higher** - [Download](https://nodejs.org/)
- **PostgreSQL 13 or higher** - [Download](https://postgresql.org/download/)
- **Maven 3.6 or higher** - [Download](https://maven.apache.org/download.cgi)
- **Git** - [Download](https://git-scm.com/downloads)

### Verify Prerequisites

```bash
# Check Java version
java --version

# Check Node.js version
node --version

# Check PostgreSQL version
psql --version

# Check Maven version
mvn --version

# Check Git version
git --version
```

## 🗄️ Database Setup

### PostgreSQL Setup (Recommended)

#### 1. Install PostgreSQL

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
```

**macOS:**
```bash
brew install postgresql
brew services start postgresql
```

**Windows:**
Download and install from [PostgreSQL Downloads](https://www.postgresql.org/download/windows/)

#### 2. Create Database and User

```bash
# Switch to postgres user
sudo -u postgres psql

# Or on macOS/Windows
psql postgres
```

```sql
-- Create database
CREATE DATABASE password_vault;

-- Create user with password
CREATE USER vault_user WITH ENCRYPTED PASSWORD 'vault_password_123';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE password_vault TO vault_user;

-- Grant schema privileges (PostgreSQL 15+)
\c password_vault
GRANT ALL ON SCHEMA public TO vault_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO vault_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO vault_user;

-- Exit
\q
```

#### 3. Test Connection

```bash
psql -h localhost -U vault_user -d password_vault
# Enter password: vault_password_123
# Should connect successfully
```

### MySQL Setup (Alternative)

If you prefer MySQL:

#### 1. Install MySQL

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

**macOS:**
```bash
brew install mysql
brew services start mysql
```

#### 2. Create Database and User

```bash
sudo mysql
```

```sql
CREATE DATABASE password_vault;
CREATE USER 'vault_user'@'localhost' IDENTIFIED BY 'vault_password_123';
GRANT ALL PRIVILEGES ON password_vault.* TO 'vault_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

## 🔧 Backend Setup

### 1. Navigate to Backend Directory

```bash
cd password-vault/backend
```

### 2. Configure Environment Variables

```bash
# Copy example environment file
cp .env.example .env

# Edit the environment file
nano .env  # or use your preferred editor
```

Update `.env` with your database credentials:

```bash
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/password_vault
DATABASE_USERNAME=vault_user
DATABASE_PASSWORD=vault_password_123
DATABASE_DRIVER=org.postgresql.Driver
HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# For MySQL (if using MySQL instead)
# DATABASE_URL=jdbc:mysql://localhost:3306/password_vault
# DATABASE_USERNAME=vault_user
# DATABASE_PASSWORD=vault_password_123
# DATABASE_DRIVER=com.mysql.cj.jdbc.Driver
# HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect

# JPA Configuration
DDL_AUTO=update
SHOW_SQL=true

# Server Configuration
PORT=8080

# Security Configuration (CHANGE IN PRODUCTION!)
ENCRYPTION_SECRET_KEY=dev-encryption-key-32-characters
JWT_SECRET=dev-jwt-secret-key-for-development-only-change-in-production

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173,http://127.0.0.1:3000
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=*
CORS_ALLOW_CREDENTIALS=true

# Logging Configuration
LOG_LEVEL=DEBUG
SECURITY_LOG_LEVEL=DEBUG
SQL_LOG_LEVEL=DEBUG
```

### 3. Install Dependencies and Build

```bash
# Clean and install dependencies
mvn clean install

# Skip tests for faster build (optional)
mvn clean install -DskipTests
```

### 4. Run the Backend

```bash
# Run with Maven
mvn spring-boot:run

# Or run the JAR directly
java -jar target/password-vault-backend-1.0.0.jar
```

The backend will start on `http://localhost:8080`

### 5. Verify Backend is Running

```bash
# Check health endpoint
curl http://localhost:8080/api/health

# Expected response:
{
  "success": true,
  "message": "Application is healthy",
  "data": {
    "status": "UP",
    "timestamp": "2024-01-01T12:00:00",
    "application": "Password Vault Backend",
    "version": "1.0.0"
  }
}
```

## 🎨 Frontend Setup

### 1. Navigate to Frontend Directory

```bash
cd password-vault/frontend
```

### 2. Install Dependencies

```bash
# Install all dependencies
npm install

# Or use yarn
yarn install
```

### 3. Configure Environment Variables

```bash
# Copy example environment file
cp .env.example .env.local

# Edit the environment file
nano .env.local  # or use your preferred editor
```

Update `.env.local`:

```bash
# API Configuration
VITE_API_BASE_URL=http://localhost:8080/api

# Application Configuration
VITE_APP_NAME="Password Vault"
VITE_APP_DESCRIPTION="Secure Password Manager"
VITE_APP_VERSION="1.0.0"

# Feature Flags
VITE_ENABLE_ANALYTICS=false
VITE_ENABLE_DEBUG=true
```

### 4. Run the Frontend

```bash
# Start development server
npm run dev

# Or use yarn
yarn dev
```

The frontend will start on `http://localhost:3000`

### 5. Verify Frontend is Running

Open your browser and navigate to `http://localhost:3000`. You should see the Password Vault login page.

## 🧪 Testing the Setup

### 1. Create a Test User

Navigate to `http://localhost:3000/register` and create a test account:

- **Username**: testuser
- **Email**: test@example.com
- **Password**: TestPassword123!
- **Confirm Password**: TestPassword123!

### 2. Login and Test Features

1. Login with your test credentials
2. Navigate to the dashboard
3. Try creating a password entry
4. Test the favorites feature
5. Check the settings page

### 3. Backend API Testing

You can also test the API directly:

```bash
# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "apitest",
    "email": "apitest@example.com",
    "password": "ApiTest123!",
    "confirmPassword": "ApiTest123!"
  }'

# Login (note: this uses form data for Spring Security)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "usernameOrEmail=apitest&password=ApiTest123!" \
  -c cookies.txt

# Get user info (using saved cookies)
curl -X GET http://localhost:8080/api/auth/me \
  -b cookies.txt
```

## 🔧 Development Tools

### IDE Setup

#### IntelliJ IDEA (Recommended for Backend)

1. **Import Project**: File → Open → Select `backend/pom.xml`
2. **Enable Annotation Processing**: Settings → Build → Compiler → Annotation Processors
3. **Install Plugins**:
   - Spring Boot
   - Lombok (if used)
   - SonarLint

#### VS Code (Recommended for Frontend)

1. **Open Project**: File → Open Folder → Select `frontend/`
2. **Install Extensions**:
   - ES7+ React/Redux/React-Native snippets
   - TypeScript Importer
   - Tailwind CSS IntelliSense
   - Auto Rename Tag
   - Bracket Pair Colorizer
   - GitLens
   - Thunder Client (for API testing)

### Database Management Tools

#### pgAdmin (PostgreSQL)
```bash
# Install pgAdmin
sudo apt install pgadmin4

# Or use web version
pip install pgadmin4
```

#### DBeaver (Universal)
Download from [DBeaver.io](https://dbeaver.io/download/)

Connection settings:
- **Host**: localhost
- **Port**: 5432 (PostgreSQL) or 3306 (MySQL)
- **Database**: password_vault
- **Username**: vault_user
- **Password**: vault_password_123

## 🐛 Troubleshooting

### Common Backend Issues

#### Database Connection Issues

**Error**: `Connection refused`
```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql

# Start PostgreSQL if not running
sudo systemctl start postgresql

# Check if port is open
netstat -an | grep 5432
```

**Error**: `Authentication failed`
```bash
# Reset password
sudo -u postgres psql
ALTER USER vault_user WITH PASSWORD 'vault_password_123';
```

**Error**: `Database does not exist`
```bash
# Create database
sudo -u postgres createdb password_vault
```

#### Application Startup Issues

**Error**: `Port 8080 already in use`
```bash
# Find process using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>

# Or change port in .env
PORT=8081
```

**Error**: `Encryption key invalid`
```bash
# Generate new encryption key (32 characters)
openssl rand -base64 32

# Update .env file
ENCRYPTION_SECRET_KEY=your-new-32-character-key
```

### Common Frontend Issues

#### Dependency Issues

**Error**: `Module not found`
```bash
# Clear node modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

**Error**: `ENOSPC: System limit for number of file watchers reached`
```bash
# Increase file watcher limit (Linux)
echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf
sudo sysctl -p
```

#### Build Issues

**Error**: `TypeScript errors`
```bash
# Check TypeScript configuration
npx tsc --noEmit

# Fix specific errors or temporarily skip
npm run build -- --mode development
```

#### API Connection Issues

**Error**: `CORS policy error`
- Check backend CORS configuration in `application.yml`
- Ensure frontend URL is in `CORS_ALLOWED_ORIGINS`
- Verify backend is running on correct port

**Error**: `Network Error`
- Check if backend is running (`curl http://localhost:8080/api/health`)
- Verify `VITE_API_BASE_URL` in frontend `.env.local`
- Check firewall settings

### Performance Issues

#### Backend Performance

```bash
# Increase JVM heap size
export MAVEN_OPTS="-Xmx1024m"
mvn spring-boot:run

# Or when running JAR
java -Xmx1024m -jar target/password-vault-backend-1.0.0.jar
```

#### Frontend Performance

```bash
# Clear Vite cache
rm -rf node_modules/.vite

# Rebuild
npm run build
```

## 📝 Development Workflow

### 1. Daily Development

```bash
# Start backend
cd backend && mvn spring-boot:run

# Start frontend (in new terminal)
cd frontend && npm run dev
```

### 2. Making Changes

#### Backend Changes
- Modify Java files in `src/main/java/com/passwordvault/`
- Spring Boot will auto-restart with changes
- Check logs in terminal for any errors

#### Frontend Changes
- Modify React files in `src/`
- Vite will hot-reload changes automatically
- Check browser console for any errors

### 3. Testing Changes

```bash
# Backend tests
cd backend && mvn test

# Frontend tests
cd frontend && npm test
```

### 4. Code Quality

```bash
# Backend linting (if configured)
cd backend && mvn checkstyle:check

# Frontend linting
cd frontend && npm run lint

# Fix linting issues
cd frontend && npm run lint:fix
```

## 🔄 Git Workflow

### 1. Initial Setup

```bash
# Clone repository
git clone https://github.com/yourusername/password-vault.git
cd password-vault

# Create development branch
git checkout -b develop
```

### 2. Feature Development

```bash
# Create feature branch
git checkout -b feature/user-authentication

# Make changes and commit
git add .
git commit -m "Add user authentication endpoints"

# Push to remote
git push origin feature/user-authentication
```

### 3. Code Review

```bash
# Create pull request to develop branch
# After review, merge and delete feature branch
git checkout develop
git pull origin develop
git branch -d feature/user-authentication
```

## 📚 Additional Resources

### Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [React Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Tailwind CSS Docs](https://tailwindcss.com/docs)

### Tools
- [Postman](https://www.postman.com/) - API testing
- [Insomnia](https://insomnia.rest/) - API testing alternative
- [React DevTools](https://react.dev/learn/react-developer-tools)
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools)

## 🤝 Getting Help

If you encounter issues:

1. Check this troubleshooting guide
2. Review application logs
3. Search existing GitHub issues
4. Create a new issue with:
   - Operating system
   - Java/Node.js versions
   - Complete error messages
   - Steps to reproduce

---

Happy coding! 🚀