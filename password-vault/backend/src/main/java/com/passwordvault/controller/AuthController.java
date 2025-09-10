package com.passwordvault.controller;

import com.passwordvault.dto.ApiResponse;
import com.passwordvault.dto.AuthRequest;
import com.passwordvault.dto.RegisterRequest;
import com.passwordvault.dto.UserResponse;
import com.passwordvault.service.CustomUserDetailsService;
import com.passwordvault.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for authentication endpoints
 * Handles user registration, login, logout, and authentication status
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Registration attempt for username: {}", request.getUsername());
        
        UserResponse userResponse = userService.registerUser(request);
        
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", userResponse));
    }
    
    /**
     * Login endpoint - handled by Spring Security
     * This method documents the expected request/response format
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody AuthRequest request) {
        // This method is intercepted by Spring Security's authentication filter
        // The actual authentication is handled by CustomAuthenticationSuccessHandler
        // This method serves as documentation for the API
        return ResponseEntity.ok(ApiResponse.success("This endpoint is handled by Spring Security"));
    }
    
    /**
     * Logout endpoint - handled by Spring Security
     * This method documents the expected response format
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        // This method is intercepted by Spring Security's logout filter
        // The actual logout is handled by CustomLogoutSuccessHandler
        // This method serves as documentation for the API
        return ResponseEntity.ok(ApiResponse.success("This endpoint is handled by Spring Security"));
    }
    
    /**
     * Get current authenticated user information
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("User not authenticated"));
        }
        
        CustomUserDetailsService.CustomUserPrincipal userPrincipal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        
        UserResponse userResponse = userService.getUserById(userPrincipal.getId());
        
        return ResponseEntity.ok(ApiResponse.success("User information retrieved", userResponse));
    }
    
    /**
     * Check if username is available
     */
    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkUsernameAvailability(
            @RequestParam String username) {
        
        boolean available = userService.isUsernameAvailable(username);
        Map<String, Boolean> result = Map.of("available", available);
        
        return ResponseEntity.ok(ApiResponse.success("Username availability checked", result));
    }
    
    /**
     * Check if email is available
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkEmailAvailability(
            @RequestParam String email) {
        
        boolean available = userService.isEmailAvailable(email);
        Map<String, Boolean> result = Map.of("available", available);
        
        return ResponseEntity.ok(ApiResponse.success("Email availability checked", result));
    }
    
    /**
     * Check authentication status
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        boolean authenticated = authentication != null && 
                               authentication.isAuthenticated() && 
                               !"anonymousUser".equals(authentication.getName());
        
        Map<String, Object> status = Map.of(
            "authenticated", authenticated,
            "username", authenticated ? authentication.getName() : null
        );
        
        return ResponseEntity.ok(ApiResponse.success("Authentication status", status));
    }
}