package com.vault.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final Key signingKey;
    private final String issuer;
    private final long accessTokenTtlMinutes;

    public JwtService(
        @Value("${security.jwt.secret-base64}") String secretBase64,
        @Value("${security.jwt.issuer}") String issuer,
        @Value("${security.jwt.access-token-ttl-minutes}") long accessTokenTtlMinutes
    ) {
        if (secretBase64 == null || secretBase64.isBlank()) {
            throw new IllegalStateException("JWT secret is not configured. Set SECURITY_JWT_SECRET_BASE64 env");
        }
        byte[] secretBytes = java.util.Base64.getDecoder().decode(secretBase64);
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
        this.issuer = issuer;
        this.accessTokenTtlMinutes = accessTokenTtlMinutes;
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessTokenTtlMinutes * 60);
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .signWith(signingKey)
            .compact();
    }

    public Optional<String> validateAndGetSubject(String token) {
        try {
            return Optional.ofNullable(
                Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload().getSubject()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
