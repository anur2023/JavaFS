package com.std.ecommerce.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 🔐 Secret Key (must be long & secure)
    private final String SECRET = "mysecretkeymysecretkeymysecretkey12345";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 🔹 Generate Token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Extract Email from Token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 🔹 Validate Token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 🔹 Get Claims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}