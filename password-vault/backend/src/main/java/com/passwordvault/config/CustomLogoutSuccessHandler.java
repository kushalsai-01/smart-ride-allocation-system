package com.passwordvault.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.passwordvault.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom logout success handler
 * Returns JSON response on successful logout
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Authentication authentication) throws IOException {
        
        // Create success response
        ApiResponse<Void> apiResponse = ApiResponse.success("Logout successful");
        
        // Set response properties
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // Write response
        objectMapper.writeValue(response.getWriter(), apiResponse);
        
        if (authentication != null) {
            logger.info("User logged out successfully: {}", authentication.getName());
        } else {
            logger.info("User logged out successfully");
        }
    }
}