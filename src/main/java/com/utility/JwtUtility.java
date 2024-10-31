package com.utility;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtility {
    private String secretKey = "";

    // Constructor to initialize the secret key
    public JwtUtility() {
        if (secretKey == null || secretKey.isEmpty()) {
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
                SecretKey sk = keyGen.generateKey();
                secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error initializing JWT Key ", e);
            }
        }
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // 1 hour validity
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private java.security.Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Invalid or expired token: " + e.getMessage()); // Use logging framework instead
            return false;
        }
    }

    public String validateTokenAndGetEmail(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
