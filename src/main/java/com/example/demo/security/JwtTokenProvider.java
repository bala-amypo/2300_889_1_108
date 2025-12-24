package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {

    private final Key key;
    private final long validityInMs;
    private final boolean enabled;

    // ***** TESTS REQUIRE THIS CONSTRUCTOR *****
    public JwtTokenProvider(String secretKey, long validityInMs, boolean enabled) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.validityInMs = validityInMs;
        this.enabled = enabled;
    }

    public JwtTokenProvider() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.validityInMs = 3600000L;
        this.enabled = true;
    }

    // ******** GENERATE TOKEN ********
    public String generateToken(Authentication authentication,
                                Long userId,
                                String role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("userId", userId)
                .claim("role", role)
                .claim("email", authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ******** USERNAME ********
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // ******** VALIDATION ********
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // ******** RETURN ALL CLAIMS AS MAP ********
    public Map<String, Object> getAllClaims(String token) {
        Claims claims = parseClaims(token);
        Map<String, Object> map = new HashMap<>();
        map.putAll(claims);
        return map;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
