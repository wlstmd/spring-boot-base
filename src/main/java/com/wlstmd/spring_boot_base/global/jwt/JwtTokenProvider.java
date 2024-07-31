package com.wlstmd.spring_boot_base.global.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessToken.expiration}")
    private int jwtAccessTokenExpirationMs;

    @Value("${jwt.refreshToken.expiration}")
    private int jwtRefreshTokenExpirationMs;

    public String createAccessToken(String email) {
        return createToken(email, jwtAccessTokenExpirationMs);
    }

    public String createRefreshToken(String email) {
        return createToken(email, jwtRefreshTokenExpirationMs);
    }

    private String createToken(String email, int expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}