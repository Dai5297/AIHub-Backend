package com.dai.config;

import com.dai.constant.SystemMessages;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.*;
import dev.langchain4j.web.search.WebSearchTool;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatAssistantConfig {


    private final PersistentChatMemoryStore persistentChatMemoryStore;

    private final StreamingChatLanguageModel model;

    public interface ChatAssistant {
        @SystemMessage(SystemMessages.CHAT_SYSTEM_MESSAGE)
        TokenStream chat(@MemoryId String id, @UserMessage String message, @V("current_date") String currentDate);
    }

    public interface ChatWebAssistant {
        @SystemMessage(SystemMessages.CHAT_WEB_SYSTEM_MESSAGE)
        TokenStream chat(@MemoryId String id, @UserMessage String message, @V("current_date") String currentDate);
    }

    @Bean
    public ChatAssistant chatAssistant() {

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        return AiServices.builder(ChatAssistant.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    @Bean
    public ChatWebAssistant chatWebAssistant() {

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        SearchApiWebSearchEngine engine = SearchApiWebSearchEngine.builder()
                        .engine("google")
                        .apiKey(System.getenv("SEARCHAPI_API_KEY"))
                        .build();

        return AiServices.builder(ChatWebAssistant.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .tools(new WebSearchTool(engine))
                .build();
    }
}
