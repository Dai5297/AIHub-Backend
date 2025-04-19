package com.dai.config;

import com.dai.constant.SystemMessages;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.*;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PdfAssistantConfig {

    private final QwenEmbeddingModel embeddingModel;

    private final QwenStreamingChatModel model;

    private final PersistentChatMemoryStore persistentChatMemoryStore;

    private final ElasticsearchEmbeddingStore embeddingStore;

    public interface PDFAssistant{
        @SystemMessage(SystemMessages.PDF_SYSTEM_MESSAGE)
        TokenStream chat(@MemoryId String memoryId, @UserMessage String message);
    }

    @Bean
    public PDFAssistant pdfAssistant(){
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(1)
                .minScore(0.6)
                .build();

        return AiServices.builder(PDFAssistant.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .contentRetriever(contentRetriever)
                .build();
    }
}
