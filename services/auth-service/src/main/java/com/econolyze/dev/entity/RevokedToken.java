package com.econolyze.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(schema = "auth", name = "revoked_tokens")
@AllArgsConstructor
@NoArgsConstructor
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    public String jti;

    @Column(name = "expires_at", nullable = false)
    public Instant expiresAt;


}
