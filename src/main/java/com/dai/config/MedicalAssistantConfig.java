package com.dai.config;

import com.dai.tool.MedicalTool;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MedicalAssistantConfig {

    @Autowired
    private PersistentChatMemoryStore persistentChatMemoryStore;

    @Autowired
    private StreamingChatLanguageModel model;

    @Autowired
    private MedicalTool medicalTool;

    public interface MedicalAssistant {

        @SystemMessage("""
                你是一个医疗助手，你可以调用工具帮助用户解决各种医疗问题，
                你的名字叫小e，你只能解答用户与医疗相关的问题，
                对于其他问题要以委婉的语气进行拒绝
                所有的回答要严格参考调用工具返回的结果， 如果没有返回则如实回答不知道 禁止虚构数据
                请以中文回答。
                """)
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
