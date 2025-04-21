package com.dai.config;

import com.dai.properties.PdfProperties;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmbeddingStoreConfig {

    private final PdfProperties pdfProperties;

    @Bean
    public PgVectorEmbeddingStore embeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .database(pdfProperties.getDataBase())
                .table(pdfProperties.getTable())
                .host(pdfProperties.getHost())
                .port(pdfProperties.getPort())
                .user(pdfProperties.getUser())
                .password(pdfProperties.getPassword())
                .dimension(pdfProperties.getDimension())
                .build();
    }
}
