package com.vault.controller;

import com.vault.dto.VaultDtos;
import com.vault.service.VaultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vault")
public class VaultController {
    private final VaultService vaultService;

    public VaultController(VaultService vaultService) { this.vaultService = vaultService; }

    private Long userId(Authentication auth) { return Long.parseLong(auth.getName()); }

    @GetMapping
    public ResponseEntity<List<VaultDtos.ItemResponse>> list(Authentication auth, @RequestParam(defaultValue = "false") boolean includeSecrets) {
        return ResponseEntity.ok(vaultService.list(userId(auth), includeSecrets));
    }

    @PostMapping
    public ResponseEntity<VaultDtos.ItemResponse> create(Authentication auth, @Valid @RequestBody VaultDtos.CreateOrUpdateRequest req) {
        return ResponseEntity.ok(vaultService.create(userId(auth), req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaultDtos.ItemResponse> get(Authentication auth, @PathVariable Long id, @RequestParam(defaultValue = "true") boolean includeSecrets) {
        return vaultService.get(userId(auth), id, includeSecrets).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaultDtos.ItemResponse> update(Authentication auth, @PathVariable Long id, @Valid @RequestBody VaultDtos.CreateOrUpdateRequest req) {
        return vaultService.update(userId(auth), id, req).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        return vaultService.delete(userId(auth), id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

