package fi.haagahelia.taskmaster.taskmaster.service;

import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.time.Duration;
import java.time.Instant;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.haagahelia.taskmaster.taskmaster.dto.AccessTokenPayloadDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {
    private static final long EXPIRATION_TIME = Duration.ofHours(2).toMillis(); // Set 2 hour validity period for token
    private static final String PREFIX = "Bearer "; // Prefix for JWT itoken. Used in Authorization header

    @Value("${auth.jwt-secret}") // Upload value from configuration file
    private String jwtSecret;

    public AccessTokenPayloadDto getAccessToken(String username) { // Create JWT using username
        Instant expiresAt = Instant.now().plusMillis(EXPIRATION_TIME); // Define time of validity
        String accessToken = Jwts.builder()
                .subject(username) // User details attached to token
                .expiration(Date.from(expiresAt)) // Set time of expiration
                .signWith(getSigningKey()) // Sign token with secret key
                .compact(); // Compile everything

        return new AccessTokenPayloadDto(accessToken, expiresAt); // Returns token with expiration time
    }

    public String getAuthUser(HttpServletRequest request) { // Get username from HTTP request
        String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeaderValue == null) { // if header is empty, return null
            return null;
        }

        JwtParser parser = getJwtParser(); // Parser validates and decompresses the token

        try {
            Claims claims = parser // Delete prefix and decompress the token
                    .parseSignedClaims(authorizationHeaderValue.replace(PREFIX, ""))
                    .getPayload();

            return claims.getSubject(); // Get subject (username) from claims

        } catch (Exception e) { // If decompress fails return null
            return null;
        }
    }

    private SecretKey getSigningKey() { // Create secret key with jwtSecret
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private JwtParser getJwtParser() { // Create parser for validation of token
        SecretKey secretKey = getSigningKey(); // Get secret key for signing

        return Jwts.parserBuilder() // Create JWT-parser and validate the key
                .setSigningKey(secretKey)
                .build();

    }

}
