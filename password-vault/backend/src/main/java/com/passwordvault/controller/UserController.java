package com.passwordvault.controller;

import com.passwordvault.dto.ApiResponse;
import com.passwordvault.dto.UserResponse;
import com.passwordvault.service.CustomUserDetailsService;
import com.passwordvault.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for user profile management
 * Handles user profile updates, password changes, and account settings
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Get user profile information
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(Authentication authentication) {
        Long userId = getUserId(authentication);
        UserResponse userResponse = userService.getUserById(userId);
        
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", userResponse));
    }
    
    /**
     * Update user profile
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        logger.info("Updating profile for user ID: {}", userId);
        
        UserResponse userResponse = userService.updateUserProfile(
                userId, request.getFullName(), request.getEmail());
        
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", userResponse));
    }
    
    /**
     * Change user password
     */
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        logger.info("Changing password for user ID: {}", userId);
        
        userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }
    
    /**
     * Get user statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<UserService.UserStats>> getUserStats(Authentication authentication) {
        Long userId = getUserId(authentication);
        UserService.UserStats stats = userService.getUserStats(userId);
        
        return ResponseEntity.ok(ApiResponse.success("User statistics retrieved successfully", stats));
    }
    
    /**
     * Delete user account
     */
    @DeleteMapping("/account")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(
            @Valid @RequestBody DeleteAccountRequest request,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        String username = authentication.getName();
        
        logger.warn("Account deletion requested for user: {}", username);
        
        // Verify password before deletion
        userService.changePassword(userId, request.getPassword(), request.getPassword());
        
        // Delete account and all associated data
        userService.deleteUserAccount(userId);
        
        logger.warn("Account deleted for user: {}", username);
        
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
    }
    
    /**
     * Helper method to extract user ID from authentication
     */
    private Long getUserId(Authentication authentication) {
        CustomUserDetailsService.CustomUserPrincipal userPrincipal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }
    
    /**
     * DTO for profile update requests
     */
    public static class UpdateProfileRequest {
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        private String fullName;
        
        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        private String email;
        
        public String getFullName() {
            return fullName;
        }
        
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
    
    /**
     * DTO for password change requests
     */
    public static class ChangePasswordRequest {
        @NotBlank(message = "Current password is required")
        private String currentPassword;
        
        @NotBlank(message = "New password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        private String newPassword;
        
        @NotBlank(message = "Password confirmation is required")
        private String confirmPassword;
        
        public String getCurrentPassword() {
            return currentPassword;
        }
        
        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
        
        public String getConfirmPassword() {
            return confirmPassword;
        }
        
        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
        
        public boolean isPasswordMatching() {
            return newPassword != null && newPassword.equals(confirmPassword);
        }
    }
    
    /**
     * DTO for account deletion requests
     */
    public static class DeleteAccountRequest {
        @NotBlank(message = "Password is required to delete account")
        private String password;
        
        @NotBlank(message = "Confirmation is required")
        private String confirmation;
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public String getConfirmation() {
            return confirmation;
        }
        
        public void setConfirmation(String confirmation) {
            this.confirmation = confirmation;
        }
        
        public boolean isConfirmed() {
            return "DELETE".equals(confirmation);
        }
    }
}