package com.econolyze.dev.interfaces.rest.resource;

import com.econolyze.dev.application.services.ChatService;
import com.econolyze.dev.interfaces.rest.dto.ChatRequest;
import io.smallrye.common.annotation.Blocking;
import jakarta.json.JsonNumber;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@Path("/ai/chat")
@ApplicationScoped
public class ChatResource {

    @Inject
    ChatService chatService;
    @Inject
    JsonWebToken jwt;

    @POST
    @Blocking
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> chat(ChatRequest chatRequest) {
        JsonNumber userIdClaim = jwt.getClaim("userId");
        Long userId = userIdClaim.longValue();

        return chatService.chat(chatRequest.message(), userId);
    }
}
