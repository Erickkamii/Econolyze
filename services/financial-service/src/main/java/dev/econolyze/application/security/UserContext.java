package dev.econolyze.application.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
public class UserContext {

    private static final Logger LOG = Logger.getLogger(UserContext.class);

    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken jwt;

    public Long getUserId() {
        if (identity == null || identity.isAnonymous()) {
            LOG.error("Usuário não autenticado");
            return null;
        }

        try {
            Object claim = jwt.getClaim("userId");
            if (claim != null) {
                return Long.parseLong(claim.toString());
            }
        } catch (Exception e) {
            LOG.error("Erro ao extrair userId do token", e);
        }

        LOG.error("Token JWT não contém userId");
        return null;
    }
}
