package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

 private final Key secretKey;
 private final long validityInMs;
 private final boolean someFlag;

 public JwtTokenProvider() {
  this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  this.validityInMs = 3600000;
  this.someFlag = true;
 }

 public JwtTokenProvider(String secret, long validityInMs, boolean someFlag) {
  this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
  this.validityInMs = validityInMs;
  this.someFlag = someFlag;
 }

 public String generateToken(Long userId, String email, String role) {
  Map<String, Object> claims = new HashMap<>();
  claims.put("userId", userId);
  claims.put("email", email);
  claims.put("role", role);

  Date now = new Date();
  Date expiry = new Date(now.getTime() + validityInMs);

  return Jwts.builder()
          .setClaims(claims)
          .setIssuedAt(now)
          .setExpiration(expiry)
          .signWith(secretKey)
          .compact();
 }

 public boolean validateToken(String token) {
  try {
   Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
   return true;
  } catch (Exception e) {
   return false;
  }
 }

 public Claims getClaimsFromToken(String token) {
  return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
 }
}
