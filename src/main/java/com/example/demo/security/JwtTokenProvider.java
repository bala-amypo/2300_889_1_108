package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {

    private String jwtSecret = "DefaultSecretKeyForJwtDemoApplication123456";
    private long jwtExpirationMs = 3600000; // 1 hour
    private boolean enabled = true;

    private Key key;

    public JwtTokenProvider() {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ⭐ TEST EXPECTED CONSTRUCTOR
    public JwtTokenProvider(String secret, long expiration, boolean enabled) {
        this.jwtSecret = secret;
        this.jwtExpirationMs = expiration;
        this.enabled = enabled;
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication, Long userId, String role) {
        if (!enabled) return "";

        String email = authentication.getName();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("email", email);

        return Jwts.builder()
                .setSubject(email)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            if (!enabled) return false;
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    public Map<String, Object> getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
