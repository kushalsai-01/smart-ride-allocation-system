package com.passwordvault.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA configuration for entity auditing and repository scanning
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.passwordvault.repository")
@EnableTransactionManagement
public class JpaConfig {
    // JPA configuration is handled by Spring Boot auto-configuration
    // This class is mainly for enabling auditing and explicit repository scanning
}