package com.passwordvault.controller;

import com.passwordvault.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check controller for monitoring application status
 */
@RestController
public class HealthController {
    
    /**
     * Simple health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "application", "Password Vault Backend",
            "version", "1.0.0"
        );
        
        return ResponseEntity.ok(ApiResponse.success("Application is healthy", health));
    }
}