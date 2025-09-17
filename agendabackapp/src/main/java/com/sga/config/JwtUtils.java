package com.sga.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera la clave de firma asegurando que tenga al menos 256 bits.
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        if (keyBytes.length < 32) { // 32 bytes = 256 bits
            throw new WeakKeyException("JWT secret must be at least 32 bytes (256 bits).");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT para el usuario especificado.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el username desde un token JWT válido.
     */
    public String getUserNameFromJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida un token JWT.
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Podés loggear el error para debug
            System.err.println("JWT inválido: " + e.getMessage());
            return false;
        }
    }
}
