package com.dai.config;

import com.dai.tool.CustomerService;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServiceAssistantConfig {

    private final QwenStreamingChatModel model;

    private final CustomerService customerService;

    private final PersistentChatMemoryStore persistentChatMemoryStore;

    public interface ServiceAssistant {
        TokenStream chat(@MemoryId String id, @UserMessage String message);
    }

    @Bean
    public ServiceAssistant assistant() {
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        return AiServices.builder(ServiceAssistant.class)
                .streamingChatLanguageModel(model)
                .tools(customerService)
                .chatMemoryProvider(chatMemoryProvider)
                .build();

    }
}
