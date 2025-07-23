package com.econolyze.dev.util;

import io.smallrye.jwt.build.Jwt;

import java.util.Arrays;
import java.util.HashSet;

public class TokenGenerator {

    public static String generate(String username, String roles){
        String token =
                Jwt.upn(username)
                .groups(new HashSet<>(Arrays.asList(roles.split(","))))
                .sign();
        return token;
    }
}
