package com.vault.service;

import com.vault.dto.AuthDtos;
import com.vault.model.UserAccount;
import com.vault.repository.UserAccountRepository;
import com.vault.security.JwtService;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserAccountRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserAccount register(AuthDtos.RegisterRequest req) {
        if (userRepo.existsByEmail(req.email.toLowerCase())) {
            throw new IllegalArgumentException("Email already in use");
        }
        UserAccount u = new UserAccount();
        u.setEmail(req.email.toLowerCase());
        u.setPasswordHash(passwordEncoder.encode(req.password));
        return userRepo.save(u);
    }

    public Optional<UserAccount> authenticate(String email, String password) {
        return userRepo.findByEmail(email.toLowerCase())
            .filter(u -> passwordEncoder.matches(password, u.getPasswordHash()));
    }

    public String createSessionToken(UserAccount user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        return jwtService.generateToken(String.valueOf(user.getId()), claims);
    }

    public Optional<UserAccount> findById(Long id) { return userRepo.findById(id); }
}
