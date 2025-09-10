package com.passwordvault.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.passwordvault.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom access denied handler
 * Returns JSON response for forbidden requests
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void handle(HttpServletRequest request, 
                      HttpServletResponse response, 
                      AccessDeniedException accessDeniedException) throws IOException {
        
        // Create error response
        ApiResponse<Void> apiResponse = ApiResponse.error("Access denied");
        
        // Set response properties
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // Write response
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}