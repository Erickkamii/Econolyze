package dev.econolyze.application.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@Provider
@Priority(Priorities.AUTHENTICATION)
public class InternalAuthFilter implements ContainerRequestFilter {
    @ConfigProperty(name = "internal.secret")
    String secret;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (!requestContext.getUriInfo().getPath().contains("/internal/")) return;

        String auth = requestContext.getHeaderString("X-Internal-Token");

        if (auth == null || auth.isBlank()) {
            abort(requestContext);
            return;
        }


        try {
            var claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(auth)
                    .getPayload();

            if (!"ai-service".equals(claims.get("service", String.class))) {
                abort(requestContext);
            }
        } catch (Exception e) {
            abort(requestContext);
        }
    }

    private void abort(ContainerRequestContext context){
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}
