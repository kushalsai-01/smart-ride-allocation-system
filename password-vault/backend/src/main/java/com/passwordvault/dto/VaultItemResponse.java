package com.passwordvault.dto;

import com.passwordvault.entity.VaultItem;
import com.passwordvault.entity.VaultItemType;
import com.passwordvault.util.EncryptionUtil;

import java.time.LocalDateTime;

/**
 * DTO for vault item responses
 * Contains decrypted sensitive data for client consumption
 */
public class VaultItemResponse {
    
    private Long id;
    private String title;
    private VaultItemType type;
    
    // Decrypted sensitive fields
    private String username;
    private String password;
    private String email;
    private String url;
    private String notes;
    
    // Non-sensitive metadata
    private String description;
    private Boolean isFavorite;
    private String category;
    private String tags;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastAccessedAt;
    
    // Constructors
    public VaultItemResponse() {}
    
    public VaultItemResponse(VaultItem vaultItem, EncryptionUtil encryptionUtil) {
        this.id = vaultItem.getId();
        this.title = vaultItem.getTitle();
        this.type = vaultItem.getType();
        this.description = vaultItem.getDescription();
        this.isFavorite = vaultItem.getIsFavorite();
        this.category = vaultItem.getCategory();
        this.tags = vaultItem.getTags();
        this.createdAt = vaultItem.getCreatedAt();
        this.updatedAt = vaultItem.getUpdatedAt();
        this.lastAccessedAt = vaultItem.getLastAccessedAt();
        
        // Decrypt sensitive fields
        this.username = encryptionUtil.decrypt(vaultItem.getEncryptedUsername());
        this.password = encryptionUtil.decrypt(vaultItem.getEncryptedPassword());
        this.email = encryptionUtil.decrypt(vaultItem.getEncryptedEmail());
        this.url = encryptionUtil.decrypt(vaultItem.getEncryptedUrl());
        this.notes = encryptionUtil.decrypt(vaultItem.getEncryptedNotes());
    }
    
    /**
     * Constructor for list view (excludes sensitive data like password)
     */
    public static VaultItemResponse forList(VaultItem vaultItem, EncryptionUtil encryptionUtil) {
        VaultItemResponse response = new VaultItemResponse();
        response.id = vaultItem.getId();
        response.title = vaultItem.getTitle();
        response.type = vaultItem.getType();
        response.description = vaultItem.getDescription();
        response.isFavorite = vaultItem.getIsFavorite();
        response.category = vaultItem.getCategory();
        response.tags = vaultItem.getTags();
        response.createdAt = vaultItem.getCreatedAt();
        response.updatedAt = vaultItem.getUpdatedAt();
        response.lastAccessedAt = vaultItem.getLastAccessedAt();
        
        // Only decrypt non-password sensitive fields for list view
        response.username = encryptionUtil.decrypt(vaultItem.getEncryptedUsername());
        response.email = encryptionUtil.decrypt(vaultItem.getEncryptedEmail());
        response.url = encryptionUtil.decrypt(vaultItem.getEncryptedUrl());
        
        // Don't include password and notes in list view for security
        response.password = null;
        response.notes = null;
        
        return response;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public VaultItemType getType() {
        return type;
    }
    
    public void setType(VaultItemType type) {
        this.type = type;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsFavorite() {
        return isFavorite;
    }
    
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }
    
    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
    
    @Override
    public String toString() {
        return "VaultItemResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", category='" + category + '\'' +
                ", isFavorite=" + isFavorite +
                ", hasPassword=" + (password != null && !password.isEmpty()) +
                ", hasNotes=" + (notes != null && !notes.isEmpty()) +
                '}';
    }
}