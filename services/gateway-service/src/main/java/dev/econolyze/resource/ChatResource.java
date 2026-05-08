package dev.econolyze.resource;

import dev.econolyze.client.AiClient;
import dev.econolyze.dto.request.ChatRequest;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/api/ai")
@Authenticated
public class ChatResource {

    @Inject
    @RestClient
    AiClient aiClient;

    @POST
    @Path("/chat")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> chat(@Context HttpHeaders headers, ChatRequest request) {
        String authorization = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        return aiClient.chat(authorization, request);
    }
}