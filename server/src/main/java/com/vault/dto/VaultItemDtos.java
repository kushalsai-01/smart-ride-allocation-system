package com.vault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VaultItemDtos {
    public static class CreateRequest {
        @NotBlank @Size(max = 200)
        public String title;
        @NotBlank @Size(max = 100)
        public String type;
        @NotBlank
        public String payload; // plaintext JSON to encrypt
        public boolean favorite;
    }

    public static class UpdateRequest {
        @NotBlank @Size(max = 200)
        public String title;
        @NotBlank @Size(max = 100)
        public String type;
        @NotNull
        public Boolean favorite;
        public String payload; // optional plaintext to re-encrypt
    }

    public static class Response {
        public Long id;
        public String title;
        public String type;
        public boolean favorite;
        public String payload; // decrypted plain JSON
        public Response(Long id, String title, String type, boolean favorite, String payload) {
            this.id = id; this.title = title; this.type = type; this.favorite = favorite; this.payload = payload;
        }
    }
}
