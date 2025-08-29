package com.novara.novara_demo.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String lookup;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private Instant expiryDate;
    @Column(nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID familyId;
    @Column(nullable = false)
    private boolean revoked = false;

    public RefreshToken() {
    }

    public RefreshToken(String lookup, String token, String username, Instant expiryDate, UUID familyId) {
        this.lookup = lookup;
        this.token = token;
        this.username = username;
        this.expiryDate = expiryDate;
        this.familyId = familyId;
    }

    public Long getId() {
        return id;
    }

    public RefreshToken setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLookup() {
        return lookup;
    }

    public RefreshToken setLookup(String lookup) {
        this.lookup = lookup;
        return this;
    }

    public String getToken() {
        return token;
    }

    public RefreshToken setToken(String token) {
        this.token = token;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public RefreshToken setUsername(String username) {
        this.username = username;
        return this;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public RefreshToken setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public UUID getFamilyId() {
        return familyId;
    }

    public RefreshToken setFamilyId(UUID familyId) {
        this.familyId = familyId;
        return this;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public RefreshToken setRevoked(boolean revoked) {
        this.revoked = revoked;
        return this;
    }
}
