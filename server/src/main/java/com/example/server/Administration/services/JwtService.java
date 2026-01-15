package com.example.server.Administration.services;

import com.example.server.Administration.config.JwtProperties;
import com.example.server.Administration.converters.RoleConverter;
import com.example.server.Administration.model.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("role", RoleConverter.roleFor(user).name())
                .claim("login", user.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration().getAccess()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("type", "refresh")
                .claim("login", user.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration().getRefresh()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(parseClaims(token).get("type"));
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
