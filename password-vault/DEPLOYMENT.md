# Deployment Guide

This guide covers deploying the Password Vault application to various cloud platforms.

## 📋 Pre-deployment Checklist

### Security Configuration
- [ ] Generate secure encryption keys (32+ characters)
- [ ] Set strong JWT secrets (64+ characters)
- [ ] Configure production database with secure credentials
- [ ] Set up HTTPS certificates
- [ ] Configure CORS for production domains only
- [ ] Enable secure session cookies
- [ ] Set up environment-specific configurations

### Application Configuration
- [ ] Set `SPRING_PROFILES_ACTIVE=prod` for backend
- [ ] Configure production database URLs
- [ ] Set up health check endpoints
- [ ] Configure logging levels for production
- [ ] Set up monitoring and alerting

## 🔧 Environment Variables

### Backend Environment Variables

```bash
# Required
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://host:port/database
DATABASE_USERNAME=username
DATABASE_PASSWORD=password
ENCRYPTION_SECRET_KEY=your-256-bit-secret-key-32-characters-long
JWT_SECRET=your-jwt-secret-key-64-characters-minimum

# Optional
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com
PORT=8080
LOG_LEVEL=INFO
COOKIE_SECURE=true
```

### Frontend Environment Variables

```bash
VITE_API_BASE_URL=https://your-backend-domain.com/api
VITE_APP_NAME="Password Vault"
VITE_APP_VERSION="1.0.0"
```

## 🚀 Backend Deployment Options

### Option 1: Railway

Railway provides easy deployment with automatic HTTPS and database provisioning.

#### Step 1: Install Railway CLI
```bash
npm install -g @railway/cli
railway login
```

#### Step 2: Prepare Project
```bash
cd backend
railway init
```

#### Step 3: Configure Database
```bash
# Add PostgreSQL service
railway add postgresql

# Get database URL
railway variables
```

#### Step 4: Set Environment Variables
```bash
railway variables set SPRING_PROFILES_ACTIVE=prod
railway variables set ENCRYPTION_SECRET_KEY=$(openssl rand -base64 32)
railway variables set JWT_SECRET=$(openssl rand -base64 64)
railway variables set CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
```

#### Step 5: Deploy
```bash
railway up
```

#### Step 6: Custom Domain (Optional)
```bash
railway domain add your-api-domain.com
```

### Option 2: Render

#### Step 1: Create render.yaml
```yaml
services:
  - type: web
    name: password-vault-api
    env: java
    region: oregon
    plan: starter
    buildCommand: ./mvnw clean install -DskipTests
    startCommand: java -Dserver.port=$PORT -jar target/password-vault-backend-1.0.0.jar
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
      - key: CORS_ALLOWED_ORIGINS
        value: https://your-frontend-domain.com

databases:
  - name: password-vault-db
    databaseName: password_vault
    user: vault_user
    region: oregon
    plan: free
```

#### Step 2: Deploy
```bash
# Push to GitHub and connect to Render
git add .
git commit -m "Deploy to Render"
git push origin main
```

### Option 3: Heroku

#### Step 1: Prepare Heroku
```bash
# Install Heroku CLI
npm install -g heroku

# Login
heroku login

# Create app
heroku create your-password-vault-api
```

#### Step 2: Add PostgreSQL
```bash
heroku addons:create heroku-postgresql:mini
```

#### Step 3: Configure Environment
```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set ENCRYPTION_SECRET_KEY=$(openssl rand -base64 32)
heroku config:set JWT_SECRET=$(openssl rand -base64 64)
heroku config:set CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
```

#### Step 4: Create Procfile
```bash
echo "web: java -Dserver.port=\$PORT -jar target/password-vault-backend-1.0.0.jar" > Procfile
```

#### Step 5: Deploy
```bash
git add .
git commit -m "Deploy to Heroku"
git push heroku main
```

### Option 4: DigitalOcean App Platform

#### Step 1: Create .do/app.yaml
```yaml
name: password-vault
services:
- name: api
  source_dir: /backend
  github:
    repo: your-username/password-vault
    branch: main
  run_command: java -jar target/password-vault-backend-1.0.0.jar
  environment_slug: java
  instance_count: 1
  instance_size_slug: basic-xxs
  env:
  - key: SPRING_PROFILES_ACTIVE
    value: prod
  - key: DATABASE_URL
    value: ${db.DATABASE_URL}
  - key: ENCRYPTION_SECRET_KEY
    type: SECRET
    value: your-encryption-key
  - key: JWT_SECRET
    type: SECRET
    value: your-jwt-secret

databases:
- name: db
  engine: PG
  version: "13"
  size_slug: db-s-1vcpu-1gb
```

#### Step 2: Deploy
```bash
doctl apps create --spec .do/app.yaml
```

## 🌐 Frontend Deployment Options

### Option 1: Netlify

#### Step 1: Install Netlify CLI
```bash
npm install -g netlify-cli
netlify login
```

#### Step 2: Configure Build
Create `netlify.toml`:
```toml
[build]
  command = "npm run build"
  publish = "dist"

[build.environment]
  VITE_API_BASE_URL = "https://your-backend-domain.com/api"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200

[[headers]]
  for = "/*"
  [headers.values]
    X-Frame-Options = "DENY"
    X-Content-Type-Options = "nosniff"
    Referrer-Policy = "strict-origin-when-cross-origin"
    Content-Security-Policy = "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com; img-src 'self' data: https:;"
```

#### Step 3: Deploy
```bash
cd frontend
netlify init
netlify deploy --prod
```

#### Step 4: Custom Domain
```bash
netlify domains:add your-domain.com
```

### Option 2: Vercel

#### Step 1: Install Vercel CLI
```bash
npm install -g vercel
```

#### Step 2: Configure Build
Create `vercel.json`:
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
  ],
  "headers": [
    {
      "source": "/(.*)",
      "headers": [
        {
          "key": "X-Frame-Options",
          "value": "DENY"
        },
        {
          "key": "X-Content-Type-Options",
          "value": "nosniff"
        },
        {
          "key": "Referrer-Policy",
          "value": "strict-origin-when-cross-origin"
        }
      ]
    }
  ]
}
```

#### Step 3: Deploy
```bash
cd frontend
vercel --prod
```

### Option 3: GitHub Pages (Static Only)

#### Step 1: Configure Workflow
Create `.github/workflows/deploy.yml`:
```yaml
name: Deploy to GitHub Pages

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
        cache: 'npm'
        cache-dependency-path: frontend/package-lock.json
    
    - name: Install dependencies
      run: |
        cd frontend
        npm ci
    
    - name: Build
      run: |
        cd frontend
        npm run build
      env:
        VITE_API_BASE_URL: ${{ secrets.VITE_API_BASE_URL }}
    
    - name: Deploy to GitHub Pages
      uses: peaceiris/actions-gh-pages@v3
      if: github.ref == 'refs/heads/main'
      with:
        github_token: ${{ secrets.GITHUB_TOKEN }}
        publish_dir: frontend/dist
```

## 🔒 SSL/HTTPS Configuration

### Automatic HTTPS (Recommended)

Most modern platforms provide automatic HTTPS:
- **Netlify**: Automatic Let's Encrypt certificates
- **Vercel**: Automatic HTTPS for all deployments
- **Railway**: Automatic HTTPS with custom domains
- **Render**: Free SSL certificates

### Manual SSL Configuration

For custom servers:

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx

# Get certificate
sudo certbot --nginx -d your-domain.com -d www.your-domain.com

# Auto-renewal
sudo crontab -e
# Add: 0 12 * * * /usr/bin/certbot renew --quiet
```

## 🗄️ Database Migration

### Production Database Setup

#### PostgreSQL
```sql
-- Create production database
CREATE DATABASE password_vault_prod;
CREATE USER vault_prod_user WITH ENCRYPTED PASSWORD 'secure_production_password';
GRANT ALL PRIVILEGES ON DATABASE password_vault_prod TO vault_prod_user;

-- Enable required extensions
\c password_vault_prod;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

#### Migration from Development
```bash
# Export development data (optional)
pg_dump -h localhost -U vault_user password_vault > vault_backup.sql

# Import to production (if needed)
psql -h production-host -U vault_prod_user password_vault_prod < vault_backup.sql
```

## 📊 Monitoring and Logging

### Application Monitoring

#### Add Health Check Endpoint
The application includes health checks at `/api/health`

#### Configure Logging
```yaml
# application-prod.yml
logging:
  level:
    com.passwordvault: INFO
    org.springframework.security: WARN
  file:
    name: /var/log/password-vault/application.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

#### External Monitoring Services

**Uptime Monitoring:**
- UptimeRobot
- Pingdom
- StatusCake

**Application Performance:**
- New Relic
- DataDog
- AppDynamics

### Error Tracking

```javascript
// Frontend error tracking with Sentry (optional)
import * as Sentry from "@sentry/react";

Sentry.init({
  dsn: "YOUR_SENTRY_DSN",
  environment: "production",
});
```

## 🔄 CI/CD Pipeline

### GitHub Actions Example

Create `.github/workflows/deploy-production.yml`:

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Run backend tests
      run: |
        cd backend
        ./mvnw test
    
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
    
    - name: Run frontend tests
      run: |
        cd frontend
        npm ci
        npm test
  
  deploy-backend:
    needs: test
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    - name: Deploy to Railway
      run: |
        npm install -g @railway/cli
        railway login --token ${{ secrets.RAILWAY_TOKEN }}
        railway up --service backend
  
  deploy-frontend:
    needs: test
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    - name: Deploy to Netlify
      run: |
        cd frontend
        npm ci
        npm run build
        npx netlify-cli deploy --prod --dir=dist --auth=${{ secrets.NETLIFY_AUTH_TOKEN }} --site=${{ secrets.NETLIFY_SITE_ID }}
```

## 🚨 Troubleshooting Deployment Issues

### Common Backend Issues

**Memory Issues:**
```bash
# Increase JVM heap size
java -Xmx512m -jar target/password-vault-backend-1.0.0.jar
```

**Database Connection:**
```bash
# Check connection string format
# PostgreSQL: jdbc:postgresql://host:port/database
# MySQL: jdbc:mysql://host:port/database?useSSL=true
```

**Environment Variables:**
```bash
# List all environment variables
printenv | grep -E "(DATABASE|ENCRYPTION|JWT|CORS)"
```

### Common Frontend Issues

**API Connection:**
```javascript
// Check CORS configuration
// Ensure backend CORS_ALLOWED_ORIGINS includes frontend domain
```

**Build Issues:**
```bash
# Clear cache and rebuild
rm -rf node_modules .vite
npm install
npm run build
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
        
server:
  compression:
    enabled: true
    mime-types: text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json
    min-response-size: 1024
```

#### Frontend Optimization
```javascript
// Enable gzip compression in build
// Most platforms enable this automatically

// Optimize images
npm install -D vite-plugin-imagemin
```

## 📞 Support

If you encounter issues during deployment:

1. Check the platform-specific documentation
2. Review application logs
3. Verify environment variables
4. Test database connectivity
5. Check CORS configuration
6. Validate SSL certificates

For additional support, create an issue in the GitHub repository with:
- Platform used
- Error messages
- Environment variables (sanitized)
- Deployment logs

---

**⚠️ Security Reminder**: Always use HTTPS in production and never commit secrets to version control. Regularly update dependencies and monitor for security vulnerabilities.