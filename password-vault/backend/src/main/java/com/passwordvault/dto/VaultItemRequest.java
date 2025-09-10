package com.passwordvault.dto;

import com.passwordvault.entity.VaultItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for vault item creation and update requests
 * Contains decrypted data that will be encrypted before storage
 */
public class VaultItemRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;
    
    @NotNull(message = "Type is required")
    private VaultItemType type;
    
    // Sensitive fields (will be encrypted)
    @Size(max = 255, message = "Username cannot exceed 255 characters")
    private String username;
    
    @Size(max = 255, message = "Password cannot exceed 255 characters")
    private String password;
    
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;
    
    @Size(max = 500, message = "URL cannot exceed 500 characters")
    private String url;
    
    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;
    
    // Non-sensitive metadata
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private Boolean isFavorite = false;
    
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;
    
    @Size(max = 50, message = "Tags cannot exceed 50 characters")
    private String tags;
    
    // Constructors
    public VaultItemRequest() {}
    
    public VaultItemRequest(String title, VaultItemType type) {
        this.title = title;
        this.type = type;
    }
    
    // Getters and Setters
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
    
    @Override
    public String toString() {
        return "VaultItemRequest{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", category='" + category + '\'' +
                ", isFavorite=" + isFavorite +
                ", hasPassword=" + (password != null && !password.isEmpty()) +
                ", hasNotes=" + (notes != null && !notes.isEmpty()) +
                '}';
    }
}