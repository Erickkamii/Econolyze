package com.econolyze.dev.interfaces.scheduler;

import com.econolyze.dev.infrastructure.clients.TransactionClient;
import com.econolyze.dev.infrastructure.security.InternalTokenProvider;
import com.econolyze.dev.interfaces.rest.dto.TransactionDTO;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class RagEmbeddingService {

    @Inject
    @RestClient
    TransactionClient transactionClient;

    @Inject
    InternalTokenProvider tokenProvider;

    @Inject
    @Named("transactionStore")
    EmbeddingStore<TextSegment> embeddingStore;
    @Inject
    EmbeddingModel embeddingModel;

    @Scheduled(cron = "0 0 1 * * ?")
    public Uni<Void> indexTransactionsDaily() {
        LocalDate targetDate = LocalDate.now().minusDays(1);

        return transactionClient.getTransactionsByUserAndDate(
                        targetDate.toString(),
                        tokenProvider.getToken()
                )
                .map(RestResponse::getEntity)
                .onItem().transformToUni(transactions ->
                        Uni.createFrom().item(() -> {
                                    if (transactions == null || transactions.isEmpty()) {
                                        return null;
                                    }

                                    Map<Long, List<TransactionDTO>> byUser = transactions.stream()
                                            .collect(Collectors.groupingBy(TransactionDTO::userId));

                                    byUser.forEach((userId, userTransactions) -> {
                                        String content = buildEmbeddedContent(userTransactions);

                                        Metadata meta = Metadata.from(Map.of(
                                                "userId", userId.toString(),
                                                "sourceType", "TRANSACTION_DAILY_SUMMARY",
                                                "date", targetDate.toString()
                                        ));

                                        TextSegment textSegment = TextSegment.from(content, meta);
                                        Embedding embedding = embeddingModel.embed(textSegment).content();

                                        embeddingStore.add(embedding, textSegment);
                                    });

                                    return null;
                                })
                                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                                .replaceWithVoid()
                );
    }

    private String buildEmbeddedContent(List<TransactionDTO> transactions){
        return transactions.stream()
                .map(t-> String.format("Transação: %s, Valor: R$%s, Categoria: %s, Tipo: %s, Data: %s",
                        t.description(), t.amount(), t.category(), t.type(), t.date()))
                .collect(Collectors.joining("\n"));
    }
}
