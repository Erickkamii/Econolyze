package com.econolyze.dev.util;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class TokenGenerator {

    public static String generate(String username, String roles){
        return Jwt.upn(username)
                .groups(new HashSet<>(Arrays.asList(roles.split(","))))
                .expiresAt(Instant.now().plusSeconds(3600))
                .issuer("econolyze")
                .claim("type", "access")
                .sign();
    }

    public static String generateRefreshToken(String username){
        return Jwt.claims()
                .issuer("econolyze")
                .subject(username)
                .claim("type", "refresh")
                .expiresAt(Instant.now().plusSeconds(86400)) // 24 horas
                .sign();
    }

}
