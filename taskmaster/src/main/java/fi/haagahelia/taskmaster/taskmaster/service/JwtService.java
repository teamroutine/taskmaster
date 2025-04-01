package fi.haagahelia.taskmaster.taskmaster.service;

import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.haagahelia.taskmaster.taskmaster.dto.AccessTokenPayloadDto;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {
    private final long EXPIRATION_TIME = Duration.ofHours(8).toMillis();
    private final String PREFIX = "Bearer ";

    @Value("${auth.jwt-secret}")
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
            String user = parser
                    .parseClaimsJws(authorizationHeaderValue.replace(PREFIX, ""))
                    .getBody().getSubject();

            return user;

        } catch (Exception e) {
            return null;
        }

    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build();
    }

}
