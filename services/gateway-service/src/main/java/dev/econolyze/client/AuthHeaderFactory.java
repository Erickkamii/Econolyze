package dev.econolyze.client;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

@RequestScoped
public class AuthHeaderFactory implements ClientHeadersFactory {

    @Inject
    SecurityIdentity identity;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
                                                 MultivaluedMap<String, String> clientOutgoingHeaders) {

        MultivaluedMap<String, String> result = new MultivaluedHashMap<>(clientOutgoingHeaders);

        if (identity != null && !identity.isAnonymous()) {
            Object rawToken = identity.getAttribute("raw_token");
            if (rawToken != null) {
                result.putSingle("Authorization", "Bearer " + rawToken.toString());
            }
        }

        return result;
    }
}
