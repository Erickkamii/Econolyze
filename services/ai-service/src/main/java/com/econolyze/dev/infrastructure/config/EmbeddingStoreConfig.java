package com.econolyze.dev.infrastructure.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageMode;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.sql.DataSource;
import java.util.List;

@ApplicationScoped
public class EmbeddingStoreConfig {

    @ConfigProperty(name = "embedding.store.host")
    String host;

    @ConfigProperty(name = "embedding.store.port")
    int port;

    @ConfigProperty(name = "embedding.store.database")
    String database;

    @ConfigProperty(name = "embedding.store.user")
    String user;

    @ConfigProperty(name = "embedding.store.password")
    String password;

    @Inject
    EmbeddingModel embeddingModel;

    @Inject
    DataSource dataSource;

    private MetadataStorageConfig metadataConfig() {
        return new MetadataStorageConfig() {
            @Override
            public MetadataStorageMode storageMode() {
                return MetadataStorageMode.COMBINED_JSONB;
            }

            @Override
            public List<String> columnDefinitions() {
                return List.of("metadata JSONB NULL");
            }

            @Override
            public List<String> indexes() {
                return List.of("metadata");
            }

            @Override
            public String indexType() {
                return "GIN";
            }
        };
    }

    @Produces
    @Named("transactionStore")
    @ApplicationScoped
    public EmbeddingStore<TextSegment> transactionStore() {
        return createStore("transaction_embedding");
    }

    @Produces
    @Named("conversationStore")
    @ApplicationScoped
    public EmbeddingStore<TextSegment> conversationStore() {
        return createStore("conversation_embedding");
    }

    @Produces
    @Named("profileStore")
    @ApplicationScoped
    public EmbeddingStore<TextSegment> profileStore() {
        return createStore("profile_embedding");
    }

    private EmbeddingStore<TextSegment> createStore(String table) {
        return PgVectorEmbeddingStore.builder()
                .host(host)
                .port(port)
                .database(database)
                .user(user)
                .password(password)
                .table("ai."+table)
                .dimension(embeddingModel.dimension())
                .createTable(true)
                .metadataStorageConfig(metadataConfig())
                .build();
    }
}