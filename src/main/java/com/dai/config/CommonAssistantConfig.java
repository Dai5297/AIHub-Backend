package com.dai.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonAssistantConfig {

    @Autowired
    private ChatLanguageModel model;

    public interface CommonAssistant {
        @SystemMessage("""
                你是一个标题生成助手， 你需要更具用户的问题和对另一个AI提的问题生成一个标题，要求回答只能有标题的名字不允许有其他的任何内容
                长度控制在5-8个字，可以少于5个字但是最长不能超过8个
                """)
        String chat(String message);
    }

    @Bean
    public CommonAssistant commonAssistant() {
        return AiServices.builder(CommonAssistant.class)
                .chatLanguageModel(model)
                .build();
    }
}
