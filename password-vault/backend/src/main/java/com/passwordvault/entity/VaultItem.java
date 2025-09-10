package com.passwordvault.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * VaultItem entity representing encrypted password and note entries
 * All sensitive data is encrypted using AES-256-GCM encryption
 */
@Entity
@Table(name = "vault_items")
@EntityListeners(AuditingEntityListener.class)
public class VaultItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @Column(nullable = false)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required")
    @Column(nullable = false)
    private VaultItemType type;
    
    // Encrypted fields - stored as base64 encoded strings
    @Column(name = "encrypted_username", columnDefinition = "TEXT")
    private String encryptedUsername;
    
    @Column(name = "encrypted_password", columnDefinition = "TEXT")
    private String encryptedPassword;
    
    @Column(name = "encrypted_email", columnDefinition = "TEXT")
    private String encryptedEmail;
    
    @Column(name = "encrypted_url", columnDefinition = "TEXT")
    private String encryptedUrl;
    
    @Column(name = "encrypted_notes", columnDefinition = "TEXT")
    private String encryptedNotes;
    
    // Non-encrypted metadata
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Boolean isFavorite = false;
    
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    @Column(length = 100)
    private String category;
    
    @Size(max = 50, message = "Tags cannot exceed 50 characters")
    @Column(length = 50)
    private String tags;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime lastAccessedAt;
    
    // Many-to-One relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Constructors
    public VaultItem() {}
    
    public VaultItem(String title, VaultItemType type, User user) {
        this.title = title;
        this.type = type;
        this.user = user;
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
    
    public String getEncryptedUsername() {
        return encryptedUsername;
    }
    
    public void setEncryptedUsername(String encryptedUsername) {
        this.encryptedUsername = encryptedUsername;
    }
    
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
    
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
    
    public String getEncryptedEmail() {
        return encryptedEmail;
    }
    
    public void setEncryptedEmail(String encryptedEmail) {
        this.encryptedEmail = encryptedEmail;
    }
    
    public String getEncryptedUrl() {
        return encryptedUrl;
    }
    
    public void setEncryptedUrl(String encryptedUrl) {
        this.encryptedUrl = encryptedUrl;
    }
    
    public String getEncryptedNotes() {
        return encryptedNotes;
    }
    
    public void setEncryptedNotes(String encryptedNotes) {
        this.encryptedNotes = encryptedNotes;
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "VaultItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", category='" + category + '\'' +
                ", isFavorite=" + isFavorite +
                ", createdAt=" + createdAt +
                '}';
    }
}