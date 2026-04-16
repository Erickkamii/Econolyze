package com.econolyze.dev.repository;

import com.econolyze.dev.entity.RevokedToken;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class RevokedTokenRepository implements PanacheRepository<RevokedToken> {

    public boolean isRevoked(String jti) {
        return count("jti", jti) > 0;
    }

    // Limpa tokens já expirados da tabela (chamar via @Scheduled no serviço)
    public void deleteExpired() {
        delete("expiresAt < ?1", Instant.now());
    }
}