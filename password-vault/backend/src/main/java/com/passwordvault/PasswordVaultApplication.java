package com.passwordvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 * Main Spring Boot application class for Password Vault
 * Enables JDBC-based HTTP session management for secure authentication
 */
@SpringBootApplication
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 86400) // 24 hours session timeout
public class PasswordVaultApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PasswordVaultApplication.class, args);
    }
}