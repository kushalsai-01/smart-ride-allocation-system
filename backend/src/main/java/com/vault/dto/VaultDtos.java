package com.vault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VaultDtos {
    public static class CreateOrUpdateRequest {
        @NotBlank
        @Size(max = 200)
        public String title;
        @Size(max = 200)
        public String username;
        @Size(max = 500)
        public String url;
        public boolean favorite;
        @NotBlank
        public String password; // plaintext from client; will be encrypted server-side
        public String notes; // optional plaintext
    }

    public static class ItemResponse {
        public Long id;
        public String title;
        public String username;
        public String url;
        public boolean favorite;
        public String password; // decrypted for read responses when requested
        public String notes; // decrypted
    }
}

