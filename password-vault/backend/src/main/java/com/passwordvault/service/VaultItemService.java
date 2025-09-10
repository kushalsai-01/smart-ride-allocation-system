package com.passwordvault.service;

import com.passwordvault.dto.VaultItemRequest;
import com.passwordvault.dto.VaultItemResponse;
import com.passwordvault.entity.User;
import com.passwordvault.entity.VaultItem;
import com.passwordvault.entity.VaultItemType;
import com.passwordvault.exception.ResourceNotFoundException;
import com.passwordvault.exception.UnauthorizedAccessException;
import com.passwordvault.repository.VaultItemRepository;
import com.passwordvault.util.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for vault item management operations
 * Handles CRUD operations with encryption/decryption of sensitive data
 */
@Service
@Transactional
public class VaultItemService {
    
    private static final Logger logger = LoggerFactory.getLogger(VaultItemService.class);
    
    private final VaultItemRepository vaultItemRepository;
    private final UserService userService;
    private final EncryptionUtil encryptionUtil;
    
    @Autowired
    public VaultItemService(VaultItemRepository vaultItemRepository,
                           UserService userService,
                           EncryptionUtil encryptionUtil) {
        this.vaultItemRepository = vaultItemRepository;
        this.userService = userService;
        this.encryptionUtil = encryptionUtil;
    }
    
    /**
     * Create a new vault item
     */
    public VaultItemResponse createVaultItem(Long userId, VaultItemRequest request) {
        logger.info("Creating vault item for user ID: {}, title: {}", userId, request.getTitle());
        
        User user = userService.findById(userId);
        
        VaultItem vaultItem = new VaultItem();
        vaultItem.setTitle(request.getTitle());
        vaultItem.setType(request.getType());
        vaultItem.setDescription(request.getDescription());
        vaultItem.setIsFavorite(request.getIsFavorite() != null ? request.getIsFavorite() : false);
        vaultItem.setCategory(request.getCategory());
        vaultItem.setTags(request.getTags());
        vaultItem.setUser(user);
        
        // Encrypt sensitive fields
        vaultItem.setEncryptedUsername(encryptionUtil.encrypt(request.getUsername()));
        vaultItem.setEncryptedPassword(encryptionUtil.encrypt(request.getPassword()));
        vaultItem.setEncryptedEmail(encryptionUtil.encrypt(request.getEmail()));
        vaultItem.setEncryptedUrl(encryptionUtil.encrypt(request.getUrl()));
        vaultItem.setEncryptedNotes(encryptionUtil.encrypt(request.getNotes()));
        
        VaultItem savedItem = vaultItemRepository.save(vaultItem);
        logger.info("Vault item created successfully - ID: {}", savedItem.getId());
        
        return new VaultItemResponse(savedItem, encryptionUtil);
    }
    
    /**
     * Get all vault items for a user
     */
    @Transactional(readOnly = true)
    public List<VaultItemResponse> getAllVaultItems(Long userId) {
        List<VaultItem> vaultItems = vaultItemRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        
        return vaultItems.stream()
                .map(item -> VaultItemResponse.forList(item, encryptionUtil))
                .collect(Collectors.toList());
    }
    
    /**
     * Get vault items with pagination
     */
    @Transactional(readOnly = true)
    public Page<VaultItemResponse> getVaultItemsWithPagination(Long userId, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<VaultItem> vaultItemsPage = vaultItemRepository.findByUserIdOrderByUpdatedAtDesc(userId, pageable);
        
        return vaultItemsPage.map(item -> VaultItemResponse.forList(item, encryptionUtil));
    }
    
    /**
     * Get a specific vault item by ID
     */
    @Transactional(readOnly = true)
    public VaultItemResponse getVaultItemById(Long userId, Long itemId) {
        VaultItem vaultItem = findVaultItemByIdAndUserId(itemId, userId);
        
        // Update last accessed timestamp
        updateLastAccessedAt(itemId, userId);
        
        return new VaultItemResponse(vaultItem, encryptionUtil);
    }
    
    /**
     * Update a vault item
     */
    public VaultItemResponse updateVaultItem(Long userId, Long itemId, VaultItemRequest request) {
        logger.info("Updating vault item - ID: {}, User ID: {}", itemId, userId);
        
        VaultItem vaultItem = findVaultItemByIdAndUserId(itemId, userId);
        
        // Update non-sensitive fields
        vaultItem.setTitle(request.getTitle());
        vaultItem.setType(request.getType());
        vaultItem.setDescription(request.getDescription());
        vaultItem.setIsFavorite(request.getIsFavorite() != null ? request.getIsFavorite() : false);
        vaultItem.setCategory(request.getCategory());
        vaultItem.setTags(request.getTags());
        
        // Update encrypted sensitive fields
        vaultItem.setEncryptedUsername(encryptionUtil.encrypt(request.getUsername()));
        vaultItem.setEncryptedPassword(encryptionUtil.encrypt(request.getPassword()));
        vaultItem.setEncryptedEmail(encryptionUtil.encrypt(request.getEmail()));
        vaultItem.setEncryptedUrl(encryptionUtil.encrypt(request.getUrl()));
        vaultItem.setEncryptedNotes(encryptionUtil.encrypt(request.getNotes()));
        
        VaultItem savedItem = vaultItemRepository.save(vaultItem);
        logger.info("Vault item updated successfully - ID: {}", savedItem.getId());
        
        return new VaultItemResponse(savedItem, encryptionUtil);
    }
    
    /**
     * Delete a vault item
     */
    public void deleteVaultItem(Long userId, Long itemId) {
        logger.info("Deleting vault item - ID: {}, User ID: {}", itemId, userId);
        
        VaultItem vaultItem = findVaultItemByIdAndUserId(itemId, userId);
        vaultItemRepository.delete(vaultItem);
        
        logger.info("Vault item deleted successfully - ID: {}", itemId);
    }
    
    /**
     * Toggle favorite status of a vault item
     */
    public VaultItemResponse toggleFavorite(Long userId, Long itemId) {
        VaultItem vaultItem = findVaultItemByIdAndUserId(itemId, userId);
        vaultItem.setIsFavorite(!vaultItem.getIsFavorite());
        
        VaultItem savedItem = vaultItemRepository.save(vaultItem);
        logger.info("Toggled favorite status for vault item - ID: {}, isFavorite: {}", itemId, savedItem.getIsFavorite());
        
        return new VaultItemResponse(savedItem, encryptionUtil);
    }
    
    /**
     * Get favorite vault items for a user
     */
    @Transactional(readOnly = true)
    public List<VaultItemResponse> getFavoriteVaultItems(Long userId) {
        List<VaultItem> favoriteItems = vaultItemRepository.findByUserIdAndIsFavoriteTrueOrderByUpdatedAtDesc(userId);
        
        return favoriteItems.stream()
                .map(item -> VaultItemResponse.forList(item, encryptionUtil))
                .collect(Collectors.toList());
    }
    
    /**
     * Get vault items by type
     */
    @Transactional(readOnly = true)
    public List<VaultItemResponse> getVaultItemsByType(Long userId, VaultItemType type) {
        List<VaultItem> vaultItems = vaultItemRepository.findByUserIdAndTypeOrderByUpdatedAtDesc(userId, type);
        
        return vaultItems.stream()
                .map(item -> VaultItemResponse.forList(item, encryptionUtil))
                .collect(Collectors.toList());
    }
    
    /**
     * Get vault items by category
     */
    @Transactional(readOnly = true)
    public List<VaultItemResponse> getVaultItemsByCategory(Long userId, String category) {
        List<VaultItem> vaultItems = vaultItemRepository.findByUserIdAndCategoryIgnoreCaseOrderByUpdatedAtDesc(userId, category);
        
        return vaultItems.stream()
                .map(item -> VaultItemResponse.forList(item, encryptionUtil))
                .collect(Collectors.toList());
    }
    
    /**
     * Search vault items by title or description
     */
    @Transactional(readOnly = true)
    public List<VaultItemResponse> searchVaultItems(Long userId, String searchTerm) {
        List<VaultItem> vaultItems = vaultItemRepository.searchByTitleOrDescription(userId, searchTerm);
        
        return vaultItems.stream()
                .map(item -> VaultItemResponse.forList(item, encryptionUtil))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all distinct categories for a user
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctCategories(Long userId) {
        return vaultItemRepository.findDistinctCategoriesByUserId(userId);
    }
    
    /**
     * Get recently updated vault items
     */
    @Transactional(readOnly = true)
    public List<VaultItemResponse> getRecentlyUpdatedItems(Long userId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<VaultItem> recentItems = vaultItemRepository.findRecentlyUpdated(userId, since);
        
        return recentItems.stream()
                .map(item -> VaultItemResponse.forList(item, encryptionUtil))
                .collect(Collectors.toList());
    }
    
    /**
     * Get vault item statistics for a user
     */
    @Transactional(readOnly = true)
    public VaultItemStats getVaultItemStats(Long userId) {
        long totalItems = vaultItemRepository.countByUserId(userId);
        long passwordItems = vaultItemRepository.countByUserIdAndType(userId, VaultItemType.PASSWORD);
        long noteItems = vaultItemRepository.countByUserIdAndType(userId, VaultItemType.NOTE);
        long favoriteItems = vaultItemRepository.countByUserIdAndIsFavoriteTrue(userId);
        
        return new VaultItemStats(totalItems, passwordItems, noteItems, favoriteItems);
    }
    
    /**
     * Helper method to find vault item and verify ownership
     */
    private VaultItem findVaultItemByIdAndUserId(Long itemId, Long userId) {
        return vaultItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> {
                    // Check if item exists at all
                    if (vaultItemRepository.existsById(itemId)) {
                        throw new UnauthorizedAccessException("Access denied to vault item: " + itemId);
                    } else {
                        throw new ResourceNotFoundException("Vault item not found: " + itemId);
                    }
                });
    }
    
    /**
     * Update last accessed timestamp (async operation)
     */
    private void updateLastAccessedAt(Long itemId, Long userId) {
        try {
            vaultItemRepository.updateLastAccessedAt(itemId, userId, LocalDateTime.now());
        } catch (Exception e) {
            logger.warn("Failed to update last accessed timestamp for item: {}", itemId, e);
        }
    }
    
    /**
     * Inner class for vault item statistics
     */
    public static class VaultItemStats {
        private final long totalItems;
        private final long passwordItems;
        private final long noteItems;
        private final long favoriteItems;
        
        public VaultItemStats(long totalItems, long passwordItems, long noteItems, long favoriteItems) {
            this.totalItems = totalItems;
            this.passwordItems = passwordItems;
            this.noteItems = noteItems;
            this.favoriteItems = favoriteItems;
        }
        
        public long getTotalItems() {
            return totalItems;
        }
        
        public long getPasswordItems() {
            return passwordItems;
        }
        
        public long getNoteItems() {
            return noteItems;
        }
        
        public long getFavoriteItems() {
            return favoriteItems;
        }
    }
}