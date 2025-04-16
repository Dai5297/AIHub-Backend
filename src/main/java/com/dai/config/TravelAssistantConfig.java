package com.dai.config;

import com.dai.constant.SystemMessages;
import com.dai.properties.MapMcpProperties;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.logging.DefaultMcpLogMessageHandler;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.*;
import dev.langchain4j.service.tool.ToolProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class TravelAssistantConfig {

    private final QwenStreamingChatModel model;

    private final PersistentChatMemoryStore persistentChatMemoryStore;

    private final MapMcpProperties mcpProperties;

    public interface TravelAssistant {
        @SystemMessage(SystemMessages.TRAVEL_SYSTEM_MESSAGE)
        TokenStream chat(@MemoryId String id, @UserMessage String message, @V("current_date") String currentDate);
    }

    @Bean
    public TravelAssistant assistant() {

        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of(
                        "java",
                        "-Dspring.ai.mcp.server.stdio=true",
                        "-Dlogging.pattern.console=",
                        "-jar",
                        mcpProperties.getMcpServer()
                ))
                .logEvents(true)  // 保留日志记录以便调试
                .environment(Map.of(
                        "AMAP_API_KEY", mcpProperties.getAmapApi(),
                        "BAIDU_MAP_API_KEY", mcpProperties.getBaiduApi()
                ))
                .build();

        McpClient client = new DefaultMcpClient.Builder()
                .transport(transport)
                .logHandler(new DefaultMcpLogMessageHandler())
                .build();

        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(client)
                .build();

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory
                .builder()
                .id(memoryId)
                .maxMessages(100)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();

        return AiServices.builder(TravelAssistant.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .toolProvider(toolProvider)
                .build();

    }
}
