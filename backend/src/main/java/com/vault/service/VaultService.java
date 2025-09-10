package com.vault.service;

import com.vault.crypto.AesGcmService;
import com.vault.dto.VaultDtos;
import com.vault.model.User;
import com.vault.model.VaultItem;
import com.vault.repository.UserRepository;
import com.vault.repository.VaultItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VaultService {
    private final VaultItemRepository vaultItemRepository;
    private final UserRepository userRepository;
    private final AesGcmService aesGcmService;

    public VaultService(VaultItemRepository vaultItemRepository, UserRepository userRepository, AesGcmService aesGcmService) {
        this.vaultItemRepository = vaultItemRepository;
        this.userRepository = userRepository;
        this.aesGcmService = aesGcmService;
    }

    private User requireUser(Long userId) { return userRepository.findById(userId).orElseThrow(); }

    @Transactional
    public VaultDtos.ItemResponse create(Long userId, VaultDtos.CreateOrUpdateRequest req) {
        User user = requireUser(userId);
        VaultItem item = new VaultItem();
        item.setUser(user);
        item.setTitle(req.title);
        item.setUsername(req.username);
        item.setUrl(req.url);
        item.setFavorite(req.favorite);
        item.setEncryptedPassword(aesGcmService.encryptToBase64(req.password));
        item.setEncryptedNotes(req.notes == null ? null : aesGcmService.encryptToBase64(req.notes));
        vaultItemRepository.save(item);
        return toResponse(item, true);
    }

    public List<VaultDtos.ItemResponse> list(Long userId, boolean includeSecrets) {
        User user = requireUser(userId);
        return vaultItemRepository.findByUserOrderByUpdatedAtDesc(user).stream()
                .map(i -> toResponse(i, includeSecrets))
                .collect(Collectors.toList());
    }

    public Optional<VaultDtos.ItemResponse> get(Long userId, Long id, boolean includeSecrets) {
        User user = requireUser(userId);
        return vaultItemRepository.findByIdAndUser(id, user).map(i -> toResponse(i, includeSecrets));
    }

    @Transactional
    public Optional<VaultDtos.ItemResponse> update(Long userId, Long id, VaultDtos.CreateOrUpdateRequest req) {
        User user = requireUser(userId);
        return vaultItemRepository.findByIdAndUser(id, user).map(item -> {
            item.setTitle(req.title);
            item.setUsername(req.username);
            item.setUrl(req.url);
            item.setFavorite(req.favorite);
            if (req.password != null && !req.password.isBlank()) {
                item.setEncryptedPassword(aesGcmService.encryptToBase64(req.password));
            }
            if (req.notes != null) {
                item.setEncryptedNotes(req.notes.isBlank() ? null : aesGcmService.encryptToBase64(req.notes));
            }
            return toResponse(item, true);
        });
    }

    @Transactional
    public boolean delete(Long userId, Long id) {
        User user = requireUser(userId);
        return vaultItemRepository.findByIdAndUser(id, user).map(item -> { vaultItemRepository.delete(item); return true; }).orElse(false);
    }

    private VaultDtos.ItemResponse toResponse(VaultItem item, boolean includeSecrets) {
        VaultDtos.ItemResponse res = new VaultDtos.ItemResponse();
        res.id = item.getId();
        res.title = item.getTitle();
        res.username = item.getUsername();
        res.url = item.getUrl();
        res.favorite = item.isFavorite();
        if (includeSecrets) {
            res.password = aesGcmService.decryptFromBase64(item.getEncryptedPassword());
            res.notes = item.getEncryptedNotes() == null ? null : aesGcmService.decryptFromBase64(item.getEncryptedNotes());
        }
        return res;
    }
}

