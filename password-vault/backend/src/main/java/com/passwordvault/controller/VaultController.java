package com.passwordvault.controller;

import com.passwordvault.dto.ApiResponse;
import com.passwordvault.dto.VaultItemRequest;
import com.passwordvault.dto.VaultItemResponse;
import com.passwordvault.entity.VaultItemType;
import com.passwordvault.service.CustomUserDetailsService;
import com.passwordvault.service.VaultItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for vault item management
 * Handles CRUD operations for encrypted password and note storage
 */
@RestController
@RequestMapping("/vault")
public class VaultController {
    
    private static final Logger logger = LoggerFactory.getLogger(VaultController.class);
    
    private final VaultItemService vaultItemService;
    
    @Autowired
    public VaultController(VaultItemService vaultItemService) {
        this.vaultItemService = vaultItemService;
    }
    
    /**
     * Create a new vault item
     */
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<VaultItemResponse>> createVaultItem(
            @Valid @RequestBody VaultItemRequest request,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        logger.info("Creating vault item for user ID: {}, title: {}", userId, request.getTitle());
        
        VaultItemResponse response = vaultItemService.createVaultItem(userId, request);
        
        return ResponseEntity.ok(ApiResponse.success("Vault item created successfully", response));
    }
    
    /**
     * Get all vault items for the authenticated user
     */
    @GetMapping("/items")
    public ResponseEntity<ApiResponse<List<VaultItemResponse>>> getAllVaultItems(
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        List<VaultItemResponse> items = vaultItemService.getAllVaultItems(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Vault items retrieved successfully", items));
    }
    
    /**
     * Get vault items with pagination
     */
    @GetMapping("/items/paginated")
    public ResponseEntity<ApiResponse<Page<VaultItemResponse>>> getVaultItemsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        Page<VaultItemResponse> items = vaultItemService.getVaultItemsWithPagination(
                userId, page, size, sortBy, sortDirection);
        
        return ResponseEntity.ok(ApiResponse.success("Vault items retrieved successfully", items));
    }
    
    /**
     * Get a specific vault item by ID
     */
    @GetMapping("/items/{id}")
    public ResponseEntity<ApiResponse<VaultItemResponse>> getVaultItemById(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        VaultItemResponse item = vaultItemService.getVaultItemById(userId, id);
        
        return ResponseEntity.ok(ApiResponse.success("Vault item retrieved successfully", item));
    }
    
    /**
     * Update a vault item
     */
    @PutMapping("/items/{id}")
    public ResponseEntity<ApiResponse<VaultItemResponse>> updateVaultItem(
            @PathVariable Long id,
            @Valid @RequestBody VaultItemRequest request,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        logger.info("Updating vault item ID: {} for user ID: {}", id, userId);
        
        VaultItemResponse response = vaultItemService.updateVaultItem(userId, id, request);
        
        return ResponseEntity.ok(ApiResponse.success("Vault item updated successfully", response));
    }
    
    /**
     * Delete a vault item
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVaultItem(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        logger.info("Deleting vault item ID: {} for user ID: {}", id, userId);
        
        vaultItemService.deleteVaultItem(userId, id);
        
        return ResponseEntity.ok(ApiResponse.success("Vault item deleted successfully"));
    }
    
    /**
     * Toggle favorite status of a vault item
     */
    @PatchMapping("/items/{id}/favorite")
    public ResponseEntity<ApiResponse<VaultItemResponse>> toggleFavorite(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        VaultItemResponse response = vaultItemService.toggleFavorite(userId, id);
        
        return ResponseEntity.ok(ApiResponse.success("Favorite status updated", response));
    }
    
    /**
     * Get favorite vault items
     */
    @GetMapping("/items/favorites")
    public ResponseEntity<ApiResponse<List<VaultItemResponse>>> getFavoriteVaultItems(
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        List<VaultItemResponse> favoriteItems = vaultItemService.getFavoriteVaultItems(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Favorite items retrieved successfully", favoriteItems));
    }
    
    /**
     * Get vault items by type
     */
    @GetMapping("/items/type/{type}")
    public ResponseEntity<ApiResponse<List<VaultItemResponse>>> getVaultItemsByType(
            @PathVariable VaultItemType type,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        List<VaultItemResponse> items = vaultItemService.getVaultItemsByType(userId, type);
        
        return ResponseEntity.ok(ApiResponse.success("Items by type retrieved successfully", items));
    }
    
    /**
     * Get vault items by category
     */
    @GetMapping("/items/category/{category}")
    public ResponseEntity<ApiResponse<List<VaultItemResponse>>> getVaultItemsByCategory(
            @PathVariable String category,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        List<VaultItemResponse> items = vaultItemService.getVaultItemsByCategory(userId, category);
        
        return ResponseEntity.ok(ApiResponse.success("Items by category retrieved successfully", items));
    }
    
    /**
     * Search vault items
     */
    @GetMapping("/items/search")
    public ResponseEntity<ApiResponse<List<VaultItemResponse>>> searchVaultItems(
            @RequestParam String query,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        List<VaultItemResponse> items = vaultItemService.searchVaultItems(userId, query);
        
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", items));
    }
    
    /**
     * Get all distinct categories for the user
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getDistinctCategories(
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        List<String> categories = vaultItemService.getDistinctCategories(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", categories));
    }
    
    /**
     * Get recently updated items
     */
    @GetMapping("/items/recent")
    public ResponseEntity<ApiResponse<List<VaultItemResponse>>> getRecentlyUpdatedItems(
            @RequestParam(defaultValue = "7") int days,
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        List<VaultItemResponse> items = vaultItemService.getRecentlyUpdatedItems(userId, days);
        
        return ResponseEntity.ok(ApiResponse.success("Recent items retrieved successfully", items));
    }
    
    /**
     * Get vault statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<VaultItemService.VaultItemStats>> getVaultStats(
            Authentication authentication) {
        
        Long userId = getUserId(authentication);
        VaultItemService.VaultItemStats stats = vaultItemService.getVaultItemStats(userId);
        
        return ResponseEntity.ok(ApiResponse.success("Vault statistics retrieved successfully", stats));
    }
    
    /**
     * Get available vault item types
     */
    @GetMapping("/types")
    public ResponseEntity<ApiResponse<VaultItemType[]>> getVaultItemTypes() {
        return ResponseEntity.ok(ApiResponse.success("Vault item types retrieved", VaultItemType.values()));
    }
    
    /**
     * Helper method to extract user ID from authentication
     */
    private Long getUserId(Authentication authentication) {
        CustomUserDetailsService.CustomUserPrincipal userPrincipal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }
}