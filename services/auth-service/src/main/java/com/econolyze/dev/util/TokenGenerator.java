package com.econolyze.dev.util;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class TokenGenerator {

    public static String generate(Long id, String username, String roles){
        return Jwt.upn(username)
                .subject(String.valueOf(id))
                .groups(new HashSet<>(Arrays.asList(roles.split(","))))
                .expiresAt(Instant.now().plusSeconds(3600))
                .issuer("econolyze")
                .claim("type", "access")
                .claim("userId", id)
                .sign();
    }

    public static String generateRefreshToken(Long id, String username){
        return Jwt.claims()
                .issuer("econolyze")
                .subject(username)
                .claim("type", "refresh")
                .claim("userId", id)
                .expiresAt(Instant.now().plusSeconds(86400)) // 24 horas
                .sign();
    }

}
