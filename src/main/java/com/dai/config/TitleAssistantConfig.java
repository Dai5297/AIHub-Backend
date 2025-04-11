package com.dai.config;

import com.dai.constant.SystemMessages;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TitleAssistantConfig {

    private final ChatLanguageModel model;

    public interface TitleAssistant {
        @SystemMessage(SystemMessages.TITLE_SYSTEM_MESSAGE)
        String chat(String message);
    }

    @Bean
    public TitleAssistant commonAssistant() {
        return AiServices.builder(TitleAssistant.class)
                .chatLanguageModel(model)
                .build();
    }
}
