package com.econolyze.dev.util;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class TokenGenerator {

    @ConfigProperty(name = "app.jwt.issuer")
    String issuer;

    @ConfigProperty(name = "app.jwt.access-token-expiry", defaultValue = "3600")
    long accessTokenExpiry;

    @ConfigProperty(name = "app.jwt.refresh-token-expiry", defaultValue = "86400")
    long refreshTokenExpiry;

    public TokenPair generate(Long id, String username, String roles){
        String jti = java.util.UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(accessTokenExpiry);
        String token = Jwt.claims()
                .issuer(issuer)
                .subject(username)
                .claim("type", "access")
                .claim("userId", id)
                .claim("roles", new HashSet<>(Arrays.asList(roles.split(","))))
                .expiresAt(expiresAt)
                .claim("jti", jti)
                .sign();
        return new TokenPair(token, jti, expiresAt);
    }


    public TokenPair generateRefreshToken(Long id, String username){
        String jti = java.util.UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(refreshTokenExpiry);
        String token = Jwt.claims()
                .issuer(issuer)
                .subject(username)
                .claim("type", "refresh")
                .claim("userId", id)
                .expiresAt(expiresAt)
                .claim("jti", jti)
                .sign();
        return new TokenPair(token, jti, expiresAt);
    }


    public record TokenPair (
            String token,
            String jti,
            Instant expiresAt
    ){}
}
