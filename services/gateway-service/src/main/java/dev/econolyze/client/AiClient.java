package dev.econolyze.client;

import dev.econolyze.dto.request.ChatRequest;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/ai/chat")
@RegisterRestClient(configKey = "ai-service")
public interface AiClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    Multi<String> chat(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization,
            ChatRequest request
    );
}