package com.vault.web;

import com.vault.dto.AuthDtos;
import com.vault.model.UserAccount;
import com.vault.security.JwtService;
import com.vault.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    private final String cookieName;
    private final String cookieDomain;
    private final String cookieSameSite;
    private final boolean cookieSecure;

    public AuthController(AuthService authService, JwtService jwtService,
        @Value("${security.jwt.cookie-name}") String cookieName,
        @Value("${security.jwt.cookie-domain:}") String cookieDomain,
        @Value("${security.jwt.cookie-same-site:Lax}") String cookieSameSite,
        @Value("${security.jwt.cookie-secure:false}") boolean cookieSecure) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.cookieName = cookieName;
        this.cookieDomain = cookieDomain;
        this.cookieSameSite = cookieSameSite;
        this.cookieSecure = cookieSecure;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDtos.AuthUserResponse> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {
        UserAccount u = authService.register(req);
        return ResponseEntity.ok(new AuthDtos.AuthUserResponse(u.getId(), u.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthUserResponse> login(@Valid @RequestBody AuthDtos.LoginRequest req,
        HttpServletResponse response) {
        UserAccount u = authService.authenticate(req.email, req.password)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String token = authService.createSessionToken(u);
        addJwtCookie(response, token);
        return ResponseEntity.ok(new AuthDtos.AuthUserResponse(u.getId(), u.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        clearJwtCookie(response);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthDtos.AuthUserResponse> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        Long userId = Long.parseLong(authentication.getName());
        return authService.findById(userId)
            .map(u -> ResponseEntity.ok(new AuthDtos.AuthUserResponse(u.getId(), u.getEmail())))
            .orElse(ResponseEntity.status(401).build());
    }

    private void addJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        if (cookieDomain != null && !cookieDomain.isBlank()) cookie.setDomain(cookieDomain);
        response.addHeader("Set-Cookie", buildCookieHeader(cookie, cookieSameSite));
    }

    private void clearJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        if (cookieDomain != null && !cookieDomain.isBlank()) cookie.setDomain(cookieDomain);
        response.addHeader("Set-Cookie", buildCookieHeader(cookie, cookieSameSite));
    }

    private static String buildCookieHeader(Cookie cookie, String sameSite) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.getName()).append("=").append(cookie.getValue()).append("; Path=").append(cookie.getPath());
        if (cookie.getDomain() != null) sb.append("; Domain=").append(cookie.getDomain());
        if (cookie.getMaxAge() >= 0) sb.append("; Max-Age=").append(cookie.getMaxAge());
        if (cookie.getSecure()) sb.append("; Secure");
        if (cookie.isHttpOnly()) sb.append("; HttpOnly");
        if (sameSite != null && !sameSite.isBlank()) sb.append("; SameSite=").append(sameSite);
        return sb.toString();
    }
}
