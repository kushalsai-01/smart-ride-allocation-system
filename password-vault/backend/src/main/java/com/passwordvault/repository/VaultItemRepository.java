package com.passwordvault.repository;

import com.passwordvault.entity.VaultItem;
import com.passwordvault.entity.VaultItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for VaultItem entity
 * Provides data access methods for vault item management
 */
@Repository
public interface VaultItemRepository extends JpaRepository<VaultItem, Long> {
    
    /**
     * Find all vault items for a specific user
     */
    List<VaultItem> findByUserIdOrderByUpdatedAtDesc(Long userId);
    
    /**
     * Find all vault items for a specific user with pagination
     */
    Page<VaultItem> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * Find vault item by ID and user ID (for security)
     */
    Optional<VaultItem> findByIdAndUserId(Long id, Long userId);
    
    /**
     * Find all favorite vault items for a user
     */
    List<VaultItem> findByUserIdAndIsFavoriteTrueOrderByUpdatedAtDesc(Long userId);
    
    /**
     * Find vault items by type for a specific user
     */
    List<VaultItem> findByUserIdAndTypeOrderByUpdatedAtDesc(Long userId, VaultItemType type);
    
    /**
     * Find vault items by category for a specific user
     */
    List<VaultItem> findByUserIdAndCategoryIgnoreCaseOrderByUpdatedAtDesc(Long userId, String category);
    
    /**
     * Search vault items by title (case-insensitive)
     */
    @Query("SELECT v FROM VaultItem v WHERE v.user.id = :userId AND LOWER(v.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY v.updatedAt DESC")
    List<VaultItem> searchByTitle(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);
    
    /**
     * Search vault items by title or description (case-insensitive)
     */
    @Query("SELECT v FROM VaultItem v WHERE v.user.id = :userId AND " +
           "(LOWER(v.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY v.updatedAt DESC")
    List<VaultItem> searchByTitleOrDescription(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);
    
    /**
     * Find all distinct categories for a user
     */
    @Query("SELECT DISTINCT v.category FROM VaultItem v WHERE v.user.id = :userId AND v.category IS NOT NULL ORDER BY v.category")
    List<String> findDistinctCategoriesByUserId(@Param("userId") Long userId);
    
    /**
     * Count vault items by user
     */
    long countByUserId(Long userId);
    
    /**
     * Count vault items by user and type
     */
    long countByUserIdAndType(Long userId, VaultItemType type);
    
    /**
     * Count favorite vault items by user
     */
    long countByUserIdAndIsFavoriteTrue(Long userId);
    
    /**
     * Update last accessed timestamp
     */
    @Modifying
    @Query("UPDATE VaultItem v SET v.lastAccessedAt = :lastAccessedAt WHERE v.id = :itemId AND v.user.id = :userId")
    void updateLastAccessedAt(@Param("itemId") Long itemId, @Param("userId") Long userId, @Param("lastAccessedAt") LocalDateTime lastAccessedAt);
    
    /**
     * Delete all vault items for a user
     */
    @Modifying
    @Query("DELETE FROM VaultItem v WHERE v.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
    
    /**
     * Find recently updated vault items for a user
     */
    @Query("SELECT v FROM VaultItem v WHERE v.user.id = :userId AND v.updatedAt >= :since ORDER BY v.updatedAt DESC")
    List<VaultItem> findRecentlyUpdated(@Param("userId") Long userId, @Param("since") LocalDateTime since);
    
    /**
     * Find vault items that haven't been accessed recently
     */
    @Query("SELECT v FROM VaultItem v WHERE v.user.id = :userId AND " +
           "(v.lastAccessedAt IS NULL OR v.lastAccessedAt <= :before) " +
           "ORDER BY v.lastAccessedAt ASC NULLS FIRST")
    List<VaultItem> findNotAccessedSince(@Param("userId") Long userId, @Param("before") LocalDateTime before);
}