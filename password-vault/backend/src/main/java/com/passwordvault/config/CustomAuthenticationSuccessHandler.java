package com.passwordvault.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.passwordvault.dto.ApiResponse;
import com.passwordvault.dto.UserResponse;
import com.passwordvault.service.CustomUserDetailsService;
import com.passwordvault.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom authentication success handler
 * Returns JSON response with user details on successful login
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      Authentication authentication) throws IOException {
        
        CustomUserDetailsService.CustomUserPrincipal userPrincipal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        
        // Update last login timestamp
        userService.updateLastLoginAt(userPrincipal.getId());
        
        // Get user details
        UserResponse userResponse = userService.getUserById(userPrincipal.getId());
        
        // Create success response
        ApiResponse<UserResponse> apiResponse = ApiResponse.success("Login successful", userResponse);
        
        // Set response properties
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // Write response
        objectMapper.writeValue(response.getWriter(), apiResponse);
        
        logger.info("User logged in successfully: {}", userPrincipal.getUsername());
    }
}