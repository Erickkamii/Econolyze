package dev.econolyze.filter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    private static final Set<String> MUTATING_METHODS = Set.of("POST", "PUT", "PATCH", "DELETE");

    @ConfigProperty(name = "quarkus.http.host", defaultValue = "localhost")
    String serverHost;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        boolean isLocal = isLocalEnvironment(ctx);
        if ("OPTIONS".equals(ctx.getMethod())) {
            return;
        }

        injectAuthFromCookie(ctx);

        if (isLocal) return;

        if (MUTATING_METHODS.contains(ctx.getMethod())) {
            String path = ctx.getUriInfo().getPath();
            if (path.startsWith("api/auth")) return;

            validateCsrf(ctx);
        }
    }

    private void injectAuthFromCookie(ContainerRequestContext ctx) {
        String authorization = ctx.getHeaderString("Authorization");
        if (authorization != null && !authorization.isEmpty()) return;

        String cookieHeader = ctx.getHeaderString("Cookie");
        if (cookieHeader == null) return;

        for (String c : cookieHeader.split(";")) {
            String[] parts = c.trim().split("=", 2);
            if (parts.length == 2 && parts[0].equals("AUTH-TOKEN")) {
                String token = parts[1];
                ctx.getHeaders().putSingle("Authorization", "Bearer " + token);
                break;
            }
        }
    }

    private void validateCsrf(ContainerRequestContext ctx) {
        String xsrfHeader = ctx.getHeaderString("X-XSRF-TOKEN");
        String xsrfCookie = extractCookie(ctx.getHeaderString("Cookie"), "XSRF-TOKEN");

        if (xsrfCookie == null || xsrfHeader == null || !xsrfCookie.equals(xsrfHeader)) {
            ctx.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("CSRF token mismatch")
                    .build());
        }
    }

    private String extractCookie(String cookieHeader, String name) {
        if (cookieHeader == null) return null;
        for (String c : cookieHeader.split(";")) {
            String[] parts = c.trim().split("=", 2);
            if (parts.length == 2 && parts[0].equals(name)) {
                return parts[1];
            }
        }
        return null;
    }

    private boolean isLocalEnvironment(ContainerRequestContext ctx) {
        String hostHeader = ctx.getHeaderString("Host");
        if (hostHeader == null) return false;
        return hostHeader.contains("localhost") || hostHeader.contains("127.0.0.1");
    }

}
