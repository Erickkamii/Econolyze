package com.econolyze.dev.application.services;

import com.econolyze.dev.application.agents.FinancialAiService;
import com.econolyze.dev.application.memory.ConversationBuffer;
import com.econolyze.dev.application.tools.FinancialDataTools;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ChatService {

    @Inject
    FinancialAiService aiService;

    @Inject
    EmbeddingModel embeddingModel;

    @Inject
    ConversationBuffer conversationBuffer;

    @Inject
    @Named("transactionStore")
    EmbeddingStore<TextSegment> transactionStore;

    @Inject
    @Named("conversationStore")
    EmbeddingStore<TextSegment> conversationStore;

    @Inject
    @Named("profileStore")
    EmbeddingStore<TextSegment> profileStore;

    @Inject
    FinancialDataTools financialDataTools;

    public Uni<String> chat(String message, Long userId) {
        String financialContext = searchStore(
                transactionStore,
                message,
                userId,
                5,
                0.7,
                "Nenhum contexto de transações foi encontrado para este usuário."
        );

        String profileMemory = searchStore(
                profileStore,
                message,
                userId,
                5,
                0.6,
                "Nenhuma memória de perfil encontrada para este usuário."
        );

        String conversationMemory = searchStore(
                conversationStore,
                message,
                userId,
                5,
                0.55,
                "Nenhuma memória antiga de conversa encontrada para este usuário."
        );

        String recentConversation = conversationBuffer.getRecentMessage(userId);

        String routingHint;

        if (requiresCdiTool(message)) {
            String cdiContext = financialDataTools.getTaxaCdi();

            routingHint = """
            A pergunta exige consulta da taxa CDI atual.
            A consulta já foi feita no sistema Econolyze.
            
            Resultado da consulta:
            %s
            
            Use obrigatoriamente esse resultado na resposta.
            Não diga que não conseguiu consultar o sistema, a menos que o resultado acima informe erro.
            Não chame novamente a ferramenta getTaxaCdi.
            """.formatted(cdiContext);
        } else {
            routingHint = "Sem instrução especial de roteamento.";
        }

        String currentDate = LocalDate.now().toString();
        String currentYear = String.valueOf(LocalDate.now().getYear());

        return Uni.createFrom()
                .item(() -> aiService.chat(
                        message,
                        financialContext,
                        profileMemory,
                        conversationMemory,
                        recentConversation,
                        routingHint,
                        currentDate,
                        currentYear
                ))
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .map(this::removeToolCalls)
                .call(answer -> {
                    conversationBuffer.append(userId, "Usuário", message);
                    conversationBuffer.append(userId, "Econolyze", answer);

                    return Uni.createFrom()
                            .item(() -> {
                                saveConversationTurn(userId, message, answer);
                                return true;
                            })
                            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                            .onFailure().recoverWithItem(false)
                            .replaceWithVoid();
                });
    }

    private String removeToolCalls(String text) {
        if (text == null) {
            return "";
        }

        return text.replaceAll("<function=[^>]+>.*?</function>", "");
    }

    private void saveConversationTurn(Long userId, String userMessage, String assistantAnswer) {
        if (userMessage == null || userMessage.isBlank()) {
            return;
        }

        String memory = """
                Conversa com o usuário:
                Usuário disse: %s
                Econolyze respondeu: %s
                """.formatted(userMessage, trim(assistantAnswer, 1000));

        Metadata metadata = Metadata.from(Map.of(
                "userId", userId.toString(),
                "sourceType", "CONVERSATION_MEMORY",
                "createdAt", Instant.now().toString()
        ));

        TextSegment segment = TextSegment.from(memory, metadata);
        Embedding embedding = embeddingModel.embed(segment).content();

        conversationStore.add(embedding, segment);
    }

    private String searchStore(
            EmbeddingStore<TextSegment> store,
            String message,
            Long userId,
            int maxResults,
            double minScore,
            String emptyMessage
    ) {
        Embedding embedding = embeddingModel.embed(message).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(embedding)
                .filter(MetadataFilterBuilder.metadataKey("userId").isEqualTo(userId.toString()))
                .maxResults(maxResults)
                .minScore(minScore)
                .build();

        List<EmbeddingMatch<TextSegment>> matches = store.search(request).matches();

        if (matches.isEmpty()) {
            return emptyMessage;
        }

        return matches.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n---\n"));
    }

    private String trim(String value, int maxLength) {
        if (value == null) {
            return "";
        }

        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength) + "...";
    }

    public List<String> searchContextOnly(String message, Long userId) {
        Embedding embedding = embeddingModel.embed(message).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(embedding)
                .filter(MetadataFilterBuilder.metadataKey("userId").isEqualTo(userId.toString()))
                .maxResults(5)
                .minScore(0.7)
                .build();

        return transactionStore.search(request)
                .matches()
                .stream()
                .map(match -> match.embedded().text())
                .toList();
    }

    private boolean requiresCdiTool(String message) {
        String normalized = message.toLowerCase();

        return normalized.contains("taxa cdi")
                || normalized.contains("cdi atual")
                || normalized.contains("taxa atual")
                || normalized.contains("qual o cdi")
                || normalized.contains("quanto está o cdi")
                || normalized.contains("quanto ta o cdi")
                || normalized.contains("quanto tá o cdi")
                || normalized.contains("rendimento cdi")
                || normalized.contains("rende no cdi")
                || normalized.contains("render no cdi")
                || normalized.contains("simula")
                || normalized.contains("simule")
                || normalized.contains("projeção")
                || normalized.contains("projecao");
    }
}