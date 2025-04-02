package com.dai.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.*;
import dev.langchain4j.web.search.WebSearchTool;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
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
        @SystemMessage("""
        你的名字叫AI助手，你可以帮助用户解决各种问题，
        对应用户的问题如果你不知道答案则委婉的拒绝用户，禁止编制数据用于任何回答
        如果用户的问题与日期有关要结合今天的日期 {{current_date}}
        请以中文回答。
        """)
        TokenStream chat(@MemoryId String id, @UserMessage String message, @V("current_date") String currentDate);
    }

    public interface ChatWebAssistant {
        @SystemMessage("""
        你的名字叫AI助手，你可以帮助用户解决各种问题，
        对于用户的问题你需要联网搜索获取相关信息后再回答
        所有联网搜索的回答都要给用户提供回答依据{{current_date}}
        请以中文回答。
        """)
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
