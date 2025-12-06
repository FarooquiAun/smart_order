package com.smartorder.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secretKey;
    private final Long accessTokenExpirationMs;
    private final Long refreshTokenExpirationMs;

    public JwtService(  @Value("${app.jwt.secret}") String secretKey,
                        @Value("${app.jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
                        @Value("${app.jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs){
         this.secretKey=secretKey;
         this.accessTokenExpirationMs=accessTokenExpirationMs;
         this.refreshTokenExpirationMs=refreshTokenExpirationMs;
    }
    // ========== PUBLIC METHODS ==========

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpirationMs, Map.of());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpirationMs, Map.of("type", "refresh"));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // "sub" field
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // ========== INTERNAL HELPERS ==========

    private String generateToken(
            UserDetails userDetails,
            long expirationMs,
            Map<String, Object> extraClaims
    ) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }

    public Long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
