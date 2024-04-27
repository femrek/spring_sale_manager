package dev.faruk.auth.service;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Key;
import java.util.Scanner;

@SuppressWarnings("LombokGetterMayBeUsed")
@Slf4j
@Service
public class JwtSecretService {
    @Getter
    private final Key signKey;

    public JwtSecretService() {
        String secret;

        // read secret from environment variable
        secret = System.getenv("JWT_SECRET");
        secret = secret != null ? secret.trim() : null;

        // read secret from file if not found in environment variable
        if (secret == null || secret.isEmpty()) {
            String secretFilePath = System.getenv("JWT_SECRET_FILE");
            if (secretFilePath == null || secretFilePath.isEmpty()) {
                secretFilePath = "../jwt_secret.txt";
            }
            File secretFile = new File(secretFilePath);
            try {
                Scanner scanner = new Scanner(secretFile);
                secret = scanner.nextLine().trim();
                scanner.close();
            } catch (FileNotFoundException e) {
                log.error("JWT secret file not found: {}", secretFilePath);
            }
        }

        // throw exception if secret is not found
        if (secret == null || secret.isEmpty()) {
            throw new RuntimeException("JWT secret not found. set JWT_SECRET or JWT_SECRET_FILE environment variable.");
        }

        log.info("JWT secret loaded from environment variable or file");
        signKey = _getSignKey(secret);
    }

    /**
     * Get the key to be used for signing the token
     * @return the key to be used for signing the token
     */
    private Key _getSignKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
