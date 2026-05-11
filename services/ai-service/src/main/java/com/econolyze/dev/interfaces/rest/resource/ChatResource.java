package com.econolyze.dev.interfaces.rest.resource;

import com.econolyze.dev.application.services.ChatService;
import com.econolyze.dev.interfaces.rest.dto.ChatRequest;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.json.JsonNumber;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/ai/chat")
@ApplicationScoped
public class ChatResource {

    private static final Logger log = LoggerFactory.getLogger(ChatResource.class);
    @Inject
    ChatService chatService;
    @Inject
    JsonWebToken jwt;

    @POST
    @Blocking
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<String> chat(ChatRequest chatRequest) {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        Long userId = userIdClaim.longValue();

        return chatService.chat(chatRequest.message(), userId)
                .invoke(answer -> log.info("Resposta Gemini: "+  answer));
    }
}
