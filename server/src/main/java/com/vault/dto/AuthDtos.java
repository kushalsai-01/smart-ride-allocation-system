package com.vault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public static class RegisterRequest {
        @Email @NotBlank
        public String email;
        @NotBlank @Size(min = 8, max = 128)
        public String password;
    }

    public static class LoginRequest {
        @Email @NotBlank
        public String email;
        @NotBlank
        public String password;
    }

    public static class AuthUserResponse {
        public Long id;
        public String email;
        public AuthUserResponse(Long id, String email) { this.id = id; this.email = email; }
    }
}
