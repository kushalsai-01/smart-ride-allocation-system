package com.passwordvault.service;

import com.passwordvault.dto.RegisterRequest;
import com.passwordvault.dto.UserResponse;
import com.passwordvault.entity.User;
import com.passwordvault.exception.DuplicateResourceException;
import com.passwordvault.exception.InvalidRequestException;
import com.passwordvault.exception.ResourceNotFoundException;
import com.passwordvault.repository.UserRepository;
import com.passwordvault.repository.VaultItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for user management operations
 * Handles user registration, authentication, and profile management
 */
@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final VaultItemRepository vaultItemRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, 
                      VaultItemRepository vaultItemRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.vaultItemRepository = vaultItemRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Register a new user
     */
    public UserResponse registerUser(RegisterRequest request) {
        logger.info("Registering new user: {}", request.getUsername());
        
        // Validate password confirmation
        if (!request.isPasswordMatching()) {
            throw new InvalidRequestException("Password confirmation does not match");
        }
        
        // Check if username already exists
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName() != null ? request.getFullName().trim() : null);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getUsername());
        
        return new UserResponse(savedUser);
    }
    
    /**
     * Find user by username or email
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmailIgnoreCase(usernameOrEmail);
    }
    
    /**
     * Find user by ID
     */
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }
    
    /**
     * Get user response by ID
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = findById(userId);
        UserResponse response = new UserResponse(user);
        
        // Set vault item count
        long vaultItemCount = vaultItemRepository.countByUserId(userId);
        response.setVaultItemCount(vaultItemCount);
        
        return response;
    }
    
    /**
     * Update user's last login timestamp
     */
    public void updateLastLoginAt(Long userId) {
        userRepository.updateLastLoginAt(userId, LocalDateTime.now());
        logger.debug("Updated last login timestamp for user ID: {}", userId);
    }
    
    /**
     * Update user profile
     */
    public UserResponse updateUserProfile(Long userId, String fullName, String email) {
        User user = findById(userId);
        
        // Check if email is being changed and if it already exists
        if (email != null && !email.equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmailIgnoreCase(email)) {
                throw new DuplicateResourceException("Email already exists: " + email);
            }
            user.setEmail(email.trim().toLowerCase());
        }
        
        if (fullName != null) {
            user.setFullName(fullName.trim());
        }
        
        User savedUser = userRepository.save(user);
        logger.info("User profile updated: {}", savedUser.getUsername());
        
        return new UserResponse(savedUser);
    }
    
    /**
     * Change user password
     */
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = findById(userId);
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new InvalidRequestException("Current password is incorrect");
        }
        
        // Update password
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        logger.info("Password changed for user: {}", user.getUsername());
    }
    
    /**
     * Enable/disable user account
     */
    public UserResponse updateUserStatus(Long userId, boolean enabled) {
        User user = findById(userId);
        user.setEnabled(enabled);
        User savedUser = userRepository.save(user);
        
        logger.info("User status updated - ID: {}, Enabled: {}", userId, enabled);
        return new UserResponse(savedUser);
    }
    
    /**
     * Delete user account and all associated data
     */
    public void deleteUserAccount(Long userId) {
        User user = findById(userId);
        
        // Delete all vault items first
        vaultItemRepository.deleteAllByUserId(userId);
        
        // Delete user
        userRepository.delete(user);
        
        logger.info("User account deleted: {}", user.getUsername());
    }
    
    /**
     * Get all users (admin function)
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get user statistics
     */
    @Transactional(readOnly = true)
    public UserStats getUserStats(Long userId) {
        long totalVaultItems = vaultItemRepository.countByUserId(userId);
        long favoriteItems = vaultItemRepository.countByUserIdAndIsFavoriteTrue(userId);
        
        return new UserStats(totalVaultItems, favoriteItems);
    }
    
    /**
     * Check if username is available
     */
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsernameIgnoreCase(username);
    }
    
    /**
     * Check if email is available
     */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmailIgnoreCase(email);
    }
    
    /**
     * Inner class for user statistics
     */
    public static class UserStats {
        private final long totalVaultItems;
        private final long favoriteItems;
        
        public UserStats(long totalVaultItems, long favoriteItems) {
            this.totalVaultItems = totalVaultItems;
            this.favoriteItems = favoriteItems;
        }
        
        public long getTotalVaultItems() {
            return totalVaultItems;
        }
        
        public long getFavoriteItems() {
            return favoriteItems;
        }
    }
}