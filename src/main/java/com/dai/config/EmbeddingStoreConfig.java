package com.dai.config;

import com.dai.properties.PdfProperties;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfigurationScript;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmbeddingStoreConfig {

    private final PdfProperties pdfProperties;

    @Bean
    public ElasticsearchEmbeddingStore embeddingStore() {
        RestClient client = RestClient
                .builder(HttpHost.create(pdfProperties.getHost()))
                .build();

        return ElasticsearchEmbeddingStore.builder()
                .configuration(ElasticsearchConfigurationScript.builder().build())
                .restClient(client)
                .indexName(pdfProperties.getIndexName())
                .build();
    }
}
