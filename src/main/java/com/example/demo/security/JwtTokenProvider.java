package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public String generateToken(String username) {
        return "dummy-token";
    }

    public boolean validateToken(String token) {
        return true;
    }

    public String getUsernameFromToken(String token) {
        return "testuser";
    }
}
