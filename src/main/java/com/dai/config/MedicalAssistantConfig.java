package com.dai.config;

import com.dai.constant.SystemMessages;
import com.dai.tool.MedicalTool;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MedicalAssistantConfig {

    private final PersistentChatMemoryStore persistentChatMemoryStore;

    private final StreamingChatLanguageModel model;

    private final MedicalTool medicalTool;

    public interface MedicalAssistant {

        @SystemMessage(SystemMessages.MEDICAL_SYSTEM_MESSAGE)
        TokenStream chat(@MemoryId String id, @UserMessage String message);
    }

    @Bean
    public MedicalAssistant medicalAssistant() {

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        return AiServices.builder(MedicalAssistant.class)
                .tools(medicalTool)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
