package com.smartorder.security.token;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 500)
    private String token;
    @Column(nullable = false)
    private boolean revoked;
    @Column(nullable = false)
    private Instant expiry;
    @Column(name = "user_id",nullable = false)
    private Long userId;

    public Token(Instant expiry, String token, Long userId) {
        this.expiry = expiry;
        this.token = token;
        this.userId = userId;
    }

    public Token() {
    }

    public Instant getExpiry() {
        return expiry;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
