package com.vault.repository;

import com.vault.model.VaultItem;
import com.vault.model.UserAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaultItemRepository extends JpaRepository<VaultItem, Long> {
    List<VaultItem> findAllByOwnerOrderByCreatedAtDesc(UserAccount owner);
    Optional<VaultItem> findByIdAndOwner(Long id, UserAccount owner);
}
