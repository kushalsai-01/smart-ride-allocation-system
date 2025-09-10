package com.vault.crypto;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesGcmEncryptionService {

    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private final SecretKey secretKey;
    private final int ivBytes;
    private final SecureRandom secureRandom = new SecureRandom();

    public AesGcmEncryptionService(
        @Value("${encryption.aes.key-base64}") String keyBase64,
        @Value("${encryption.aes.iv-bytes:12}") int ivBytes
    ) {
        if (keyBase64 == null || keyBase64.isBlank()) {
            throw new IllegalStateException("AES key is not configured. Set AES_KEY_BASE64 env");
        }
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        if (keyBytes.length != 32) {
            throw new IllegalStateException("AES key must be 256-bit (32 bytes)");
        }
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
        this.ivBytes = ivBytes;
    }

    public String encryptToBase64(String plaintext) {
        try {
            byte[] iv = new byte[ivBytes];
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            buffer.put(iv);
            buffer.put(ciphertext);
            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decryptFromBase64(String base64) {
        try {
            byte[] all = Base64.getDecoder().decode(base64);
            byte[] iv = java.util.Arrays.copyOfRange(all, 0, ivBytes);
            byte[] cipherBytes = java.util.Arrays.copyOfRange(all, ivBytes, all.length);
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            byte[] plain = cipher.doFinal(cipherBytes);
            return new String(plain, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
