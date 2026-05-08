package com.econolyze.dev.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class InternalTokenProvider {

    @ConfigProperty(name = "internal.secret")
    String secret;

    private String token;

    void onStart(@Observes StartupEvent event){
        token = Jwts.builder()
                .claim("service","ai-service")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public String getToken(){
        return token;
    }
}
