package com.dai.config;

import com.dai.constant.SystemMessages;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.logging.DefaultMcpLogMessageHandler;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.*;
import dev.langchain4j.service.tool.ToolProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TravelAssistantConfig {

    private final QwenStreamingChatModel model;

    private final PersistentChatMemoryStore persistentChatMemoryStore;

    public interface TravelAssistant {
        @SystemMessage(SystemMessages.TRAVEL_SYSTEM_MESSAGE)
        TokenStream chat(@MemoryId String id, @UserMessage String message, @V("current_date") String currentDate);
    }

    @Bean
    public TravelAssistant assistant() {

        McpTransport baiduMapTransport = new HttpMcpTransport.Builder()
                .sseUrl("https://mcp.map.baidu.com/sse?ak=kHweCxtYaISWgM0BSLLglIf950vzCfXU")
                .logRequests(true)
                .build();

        McpTransport gaodeMapTransport = new HttpMcpTransport.Builder()
                .sseUrl("https://mcp.amap.com/sse?key=8981ca564f20a86ecd89a3faca9c2771")
                .logRequests(true)
                .build();

        McpClient baiduMcpClient = new DefaultMcpClient.Builder()
                .transport(baiduMapTransport)
                .logHandler(new DefaultMcpLogMessageHandler())
                .build();

        McpClient gaodeMcpClient = new DefaultMcpClient.Builder()
                .transport(gaodeMapTransport)
                .logHandler(new DefaultMcpLogMessageHandler())
                .build();

        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(baiduMcpClient, gaodeMcpClient)
                .build();

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        return AiServices.builder(TravelAssistant.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .toolProvider(toolProvider)
                .build();

    }
}
