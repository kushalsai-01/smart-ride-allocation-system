package com.vault.repository;

import com.vault.model.VaultItem;
import com.vault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VaultItemRepository extends JpaRepository<VaultItem, Long> {
    List<VaultItem> findByUserOrderByUpdatedAtDesc(User user);
    List<VaultItem> findByUserAndFavoriteTrueOrderByUpdatedAtDesc(User user);
    Optional<VaultItem> findByIdAndUser(Long id, User user);
}

