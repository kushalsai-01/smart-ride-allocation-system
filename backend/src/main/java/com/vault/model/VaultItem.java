package com.vault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "vault_items", indexes = {
        @Index(name = "idx_vaultitem_user", columnList = "user_id"),
        @Index(name = "idx_vaultitem_favorite", columnList = "favorite")
})
public class VaultItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 200)
    private String username;

    @Size(max = 500)
    private String url;

    @Column(name = "favorite", nullable = false)
    private boolean favorite = false;

    // encrypted fields (Base64 of IV+ciphertextTag)
    @Lob
    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @Lob
    @Column(name = "encrypted_notes")
    private String encryptedNotes;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void onUpdate() { this.updatedAt = Instant.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }
    public String getEncryptedNotes() { return encryptedNotes; }
    public void setEncryptedNotes(String encryptedNotes) { this.encryptedNotes = encryptedNotes; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

