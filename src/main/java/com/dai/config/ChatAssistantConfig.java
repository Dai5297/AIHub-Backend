package com.dai.config;

import com.dai.annotate.DynamicDateMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAssistantConfig {


    @Autowired
    private PersistentChatMemoryStore persistentChatMemoryStore;

    @Autowired
    private StreamingChatLanguageModel model;

    public interface ChatAssistant {
        @DynamicDateMessage
        TokenStream chat(@MemoryId String id, @UserMessage String message);
    }

    @Bean
    public ChatAssistant chatAssistant() {

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(20) // 扩大消息窗口
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        return AiServices.builder(ChatAssistant.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
