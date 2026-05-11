package dev.econolyze.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.client.AiClient;
import dev.econolyze.dto.request.ChatRequest;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@Slf4j
@Path("/api/ai")
@Authenticated
public class ChatResource {

    @Inject
    @RestClient
    AiClient aiClient;

    @Inject
    ObjectMapper objectMapper;

    @POST
    @Path("/chat")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<String> chat(@Context HttpHeaders headers, ChatRequest request) {
        String authorization = headers.getHeaderString(HttpHeaders.AUTHORIZATION);

        return aiClient.chat(authorization, request)
                .map(chunk -> {
                    try {
                        return objectMapper.writeValueAsString(Map.of("content", chunk));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .invoke(chunk -> log.info("Chunk AI: {}", chunk));
    }
}