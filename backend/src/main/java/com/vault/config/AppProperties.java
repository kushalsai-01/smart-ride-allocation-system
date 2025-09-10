package com.vault.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Jwt jwt = new Jwt();
    private final Crypto crypto = new Crypto();

    public Jwt getJwt() { return jwt; }
    public Crypto getCrypto() { return crypto; }

    public static class Jwt {
        private String secret;
        private String issuer;
        private int accessTokenTtlMinutes;

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public String getIssuer() { return issuer; }
        public void setIssuer(String issuer) { this.issuer = issuer; }
        public int getAccessTokenTtlMinutes() { return accessTokenTtlMinutes; }
        public void setAccessTokenTtlMinutes(int accessTokenTtlMinutes) { this.accessTokenTtlMinutes = accessTokenTtlMinutes; }
    }

    public static class Crypto {
        private String aesKey;
        private String salt;
        private int pbkdf2Iterations;

        public String getAesKey() { return aesKey; }
        public void setAesKey(String aesKey) { this.aesKey = aesKey; }
        public String getSalt() { return salt; }
        public void setSalt(String salt) { this.salt = salt; }
        public int getPbkdf2Iterations() { return pbkdf2Iterations; }
        public void setPbkdf2Iterations(int pbkdf2Iterations) { this.pbkdf2Iterations = pbkdf2Iterations; }
    }
}

