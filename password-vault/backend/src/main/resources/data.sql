-- Sample data for development and testing
-- This file is executed after schema creation in development mode

-- Note: In production, this file should be removed or renamed to prevent data insertion

-- Sample users (passwords are 'password123' hashed with BCrypt strength 12)
INSERT INTO users (username, email, password_hash, full_name, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at) VALUES
('demo', 'demo@example.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqyc4r6FdqC5.FvO8qPjvAW', 'Demo User', true, true, true, true, NOW(), NOW()),
('testuser', 'test@example.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqyc4r6FdqC5.FvO8qPjvAW', 'Test User', true, true, true, true, NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

-- Note: Vault items would be inserted here, but since they contain encrypted data,
-- they should be created through the API to ensure proper encryption