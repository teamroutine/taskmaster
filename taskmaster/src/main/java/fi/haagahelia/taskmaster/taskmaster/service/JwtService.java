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
    private static final long EXPIRATION_TIME = Duration.ofHours(2).toMillis(); // Set 2 hour validity period for
                                                                                // identifier
    private static final String PREFIX = "Bearer ";

    @Value("${auth.jwt-secret}") // Create secret key
    private String jwtSecret;

    public AccessTokenPayloadDto getAccessToken(String username) {
        Instant expiresAt = Instant.now().plusMillis(EXPIRATION_TIME);
        String accessToken = Jwts.builder()
                .subject(username)
                .expiration(Date.from(expiresAt))
                .signWith(getSigningKey())
                .compact();

        return new AccessTokenPayloadDto(accessToken, expiresAt);
    }

    public String getAuthUser(HttpServletRequest request) {
        String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeaderValue == null) {
            return null;
        }

        JwtParser parser = getJwtParser();

        try {
            return parser
                    .parseSignedClaims(authorizationHeaderValue.replace(PREFIX, ""))
                    .getPayload().getSubject();

        } catch (Exception e) {
            return null;
        }

    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private JwtParser getJwtParser() {
        SecretKey secretKey = getSigningKey();

        return Jwts.parser()
                .verifyWith(secretKey)
                .build();

    }

}
