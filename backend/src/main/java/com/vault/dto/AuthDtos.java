package com.vault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public static class RegisterRequest {
        @Email
        @NotBlank
        public String email;

        @NotBlank
        @Size(min = 8, max = 100)
        public String password;
    }

    public static class LoginRequest {
        @Email
        @NotBlank
        public String email;

        @NotBlank
        @Size(min = 8, max = 100)
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public String email;
        public Long userId;

        public AuthResponse(String token, String email, Long userId) {
            this.token = token;
            this.email = email;
            this.userId = userId;
        }
    }
}

