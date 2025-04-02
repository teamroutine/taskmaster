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
                .setSubject(username) // User details attached to token
                .setExpiration(Date.from(expiresAt)) // Set time of expiration
                .signWith(getSigningKey()) // Sign token with secret key
                .compact(); // Compile everything

        return new AccessTokenPayloadDto(accessToken, expiresAt); // Returns token with expiration time
    }

    public String getAuthUser(HttpServletRequest request) {
        String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeaderValue == null) {
            return null;
        }

        JwtParser parser = getJwtParser();

        try {
            return parser
                    .parseClaimsJws(authorizationHeaderValue.replace(PREFIX, ""))
                    .getBody().getSubject();

        } catch (Exception e) {
            return null;
        }
    }

    private SecretKey getSigningKey() { // Create secret key with jwtSecret
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private JwtParser getJwtParser() {
        SecretKey secretKey = getSigningKey(); // Get secret key for signing

        return Jwts.parserBuilder() // Use the new JwtParserBuilder
                .setSigningKey(secretKey)
                .build();

    }

}