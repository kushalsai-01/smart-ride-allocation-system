package com.vault.web;

import com.vault.dto.VaultItemDtos;
import com.vault.model.UserAccount;
import com.vault.service.AuthService;
import com.vault.service.VaultService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vault")
public class VaultController {
    private final VaultService vaultService;
    private final AuthService authService;

    public VaultController(VaultService vaultService, AuthService authService) {
        this.vaultService = vaultService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<VaultItemDtos.Response>> list(Authentication auth) {
        UserAccount user = requireUser(auth);
        return ResponseEntity.ok(vaultService.list(user));
    }

    @PostMapping
    public ResponseEntity<VaultItemDtos.Response> create(Authentication auth, @Valid @RequestBody VaultItemDtos.CreateRequest req) {
        UserAccount user = requireUser(auth);
        return ResponseEntity.ok(vaultService.create(user, req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaultItemDtos.Response> update(Authentication auth, @PathVariable Long id, @Valid @RequestBody VaultItemDtos.UpdateRequest req) {
        UserAccount user = requireUser(auth);
        return ResponseEntity.ok(vaultService.update(user, id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        UserAccount user = requireUser(auth);
        vaultService.delete(user, id);
        return ResponseEntity.noContent().build();
    }

    private UserAccount requireUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) throw new IllegalArgumentException("Unauthorized");
        Long id = Long.parseLong(authentication.getName());
        return authService.findById(id).orElseThrow(() -> new IllegalArgumentException("Unauthorized"));
    }
}
