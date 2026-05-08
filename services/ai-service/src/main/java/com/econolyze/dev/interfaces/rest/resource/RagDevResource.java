package com.econolyze.dev.interfaces.rest.resource;

import com.econolyze.dev.application.services.ChatService;
import com.econolyze.dev.interfaces.scheduler.RagEmbeddingService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("/dev/rag")
@ApplicationScoped
public class RagDevResource {

    @Inject
    RagEmbeddingService ragEmbeddingService;
    @Inject
    ChatService chatService;

    @POST
    @Path("/index-transactions")
    @Blocking
    public Uni<Void> indexTransactions() {
        return ragEmbeddingService.indexTransactionsDaily();
    }

    @GET
    @Path("/search")
    public List<String> search(@QueryParam("message") String message,
                               @QueryParam("userId") Long userId) {
        return chatService.searchContextOnly(message, userId);
    }
}