package com.vault.service;

import com.vault.crypto.AesGcmEncryptionService;
import com.vault.dto.VaultItemDtos;
import com.vault.model.UserAccount;
import com.vault.model.VaultItem;
import com.vault.repository.VaultItemRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class VaultService {
    private final VaultItemRepository vaultRepo;
    private final AesGcmEncryptionService enc;

    public VaultService(VaultItemRepository vaultRepo, AesGcmEncryptionService enc) {
        this.vaultRepo = vaultRepo;
        this.enc = enc;
    }

    public List<VaultItemDtos.Response> list(UserAccount owner) {
        return vaultRepo.findAllByOwnerOrderByCreatedAtDesc(owner)
            .stream()
            .map(this::toResponseDecrypted)
            .collect(Collectors.toList());
    }

    @Transactional
    public VaultItemDtos.Response create(UserAccount owner, VaultItemDtos.CreateRequest req) {
        VaultItem item = new VaultItem();
        item.setOwner(owner);
        item.setTitle(req.title);
        item.setType(req.type);
        item.setFavorite(req.favorite);
        item.setEncryptedPayload(enc.encryptToBase64(req.payload));
        return toResponseDecrypted(vaultRepo.save(item));
    }

    @Transactional
    public VaultItemDtos.Response update(UserAccount owner, Long id, VaultItemDtos.UpdateRequest req) {
        VaultItem item = vaultRepo.findByIdAndOwner(id, owner).orElseThrow(() -> new IllegalArgumentException("Not found"));
        item.setTitle(req.title);
        item.setType(req.type);
        item.setFavorite(Boolean.TRUE.equals(req.favorite));
        if (req.payload != null) {
            item.setEncryptedPayload(enc.encryptToBase64(req.payload));
        }
        return toResponseDecrypted(item);
    }

    @Transactional
    public void delete(UserAccount owner, Long id) {
        VaultItem item = vaultRepo.findByIdAndOwner(id, owner).orElseThrow(() -> new IllegalArgumentException("Not found"));
        vaultRepo.delete(item);
    }

    private VaultItemDtos.Response toResponseDecrypted(VaultItem item) {
        String payload = enc.decryptFromBase64(item.getEncryptedPayload());
        return new VaultItemDtos.Response(item.getId(), item.getTitle(), item.getType(), item.isFavorite(), payload);
    }
}
