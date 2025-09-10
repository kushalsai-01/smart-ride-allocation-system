package com.vault.service;

import com.vault.dto.AuthDtos;
import com.vault.model.User;
import com.vault.repository.UserRepository;
import com.vault.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.email.toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password));
        userRepository.save(user);
        String token = jwtService.generateToken(user.getId().toString(), Map.of("email", user.getEmail()));
        return new AuthDtos.AuthResponse(token, user.getEmail(), user.getId());
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        User user = userRepository.findByEmail(request.email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getId().toString(), Map.of("email", user.getEmail()));
        return new AuthDtos.AuthResponse(token, user.getEmail(), user.getId());
    }
}

