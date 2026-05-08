package com.econolyze.dev.infrastructure.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import org.jboss.logging.Logger;

@RequestScoped
public class UserContext {
    private static final Logger LOG = Logger.getLogger(UserContext.class);

    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken jwt;

    public Long getUserId() {
        if (identity == null || identity.isAnonymous()){
            LOG.error("Usuário não autenticado!");
            return null;
        }
        try{
            Object claim = jwt.getClaim("userId");
            if (claim != null){
                return Long.parseLong(claim.toString());
            }
        } catch (Exception e){
            LOG.error("Erro ao extrair o userId do token", e);
        }
        LOG.error("Token JWT não contém userId");
        return null;
    }

    public String getToken(){
        return "Bearer " + jwt.getRawToken();
    }
}
