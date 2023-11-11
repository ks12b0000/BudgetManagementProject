package com.wanted.budgetmanagement.global.jwt;

import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.global.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtConfig jwtConfig;

    public String generateAccessToken(User user) {
        return generateToken(user, JwtConfig.ACCESS_TOKEN_EXPIRATION_MS);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, JwtConfig.REFRESH_TOKEN_EXPIRATION_MS);
    }

    private String generateToken(User user, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .header().type("JWT")
                .and()
                .issuer(jwtConfig.getJwtIssuer())
                .issuedAt(now)
                .expiration(expiryDate)
                .subject(user.getId().toString())
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getJwtKey().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .requireIssuer(jwtConfig.getJwtIssuer())
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getJwtKey().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);
    }

}
