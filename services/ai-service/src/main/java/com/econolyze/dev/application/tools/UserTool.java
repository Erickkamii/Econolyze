package com.econolyze.dev.application.tools;

import com.econolyze.dev.infrastructure.security.UserContext;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.Instant;
import java.util.Map;

@ApplicationScoped
public class UserTool {

    @Inject
    UserContext userContext;

    @Inject
    EmbeddingModel embeddingModel;

    @Inject
    @Named("conversationStore")
    EmbeddingStore<TextSegment> conversationStore;

    @Inject
    @Named("profileStore")
    EmbeddingStore<TextSegment> profileStore;

    @Tool("""
            Salva uma memória relevante da conversa atual para uso futuro.
            Use apenas para informações que ajudem a manter continuidade da conversa.
            Não use para salvar transações, saldos ou dados financeiros temporários.
            """)
    public String saveConversationMemory(String memory) {
        if (memory == null || memory.isBlank()) {
            return "Memória ignorada: conteúdo vazio.";
        }

        Long userId = userContext.getUserId();

        Metadata metadata = Metadata.from(Map.of(
                "userId", userId.toString(),
                "sourceType", "CONVERSATION_MEMORY",
                "createdAt", Instant.now().toString()
        ));

        TextSegment segment = TextSegment.from(memory, metadata);
        Embedding embedding = embeddingModel.embed(segment).content();

        conversationStore.add(embedding, segment);

        return "Memória de conversa salva.";
    }

    @Tool("""
            Salva uma preferência, regra pessoal ou informação duradoura do usuário.
            Use para preferências de resposta, regras financeiras pessoais, metas de longo prazo e contexto estável.
            Não use para salvar perguntas comuns, cálculos pontuais ou dados financeiros que mudam frequentemente.
            """)
    public String saveUserProfileMemory(String memory) {
        if (memory == null || memory.isBlank()) {
            return "Memória ignorada: conteúdo vazio.";
        }

        Long userId = userContext.getUserId();

        Metadata metadata = Metadata.from(Map.of(
                "userId", userId.toString(),
                "sourceType", "USER_PROFILE_MEMORY",
                "createdAt", Instant.now().toString()
        ));

        TextSegment segment = TextSegment.from(memory, metadata);
        Embedding embedding = embeddingModel.embed(segment).content();

        profileStore.add(embedding, segment);

        return "Memória do usuário salva.";
    }
}