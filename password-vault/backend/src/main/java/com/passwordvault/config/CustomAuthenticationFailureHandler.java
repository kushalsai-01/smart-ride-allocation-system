package com.passwordvault.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.passwordvault.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom authentication failure handler
 * Returns JSON response with appropriate error message on login failure
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      AuthenticationException exception) throws IOException {
        
        String errorMessage = getErrorMessage(exception);
        
        // Create error response
        ApiResponse<Void> apiResponse = ApiResponse.error(errorMessage);
        
        // Set response properties
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // Write response
        objectMapper.writeValue(response.getWriter(), apiResponse);
        
        logger.warn("Authentication failed: {} - {}", exception.getClass().getSimpleName(), errorMessage);
    }
    
    private String getErrorMessage(AuthenticationException exception) {
        if (exception instanceof UsernameNotFoundException) {
            return "Invalid username or password";
        } else if (exception instanceof BadCredentialsException) {
            return "Invalid username or password";
        } else if (exception instanceof DisabledException) {
            return "Account is disabled";
        } else if (exception instanceof LockedException) {
            return "Account is locked";
        } else {
            return "Authentication failed";
        }
    }
}