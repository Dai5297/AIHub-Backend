package com.dai.service.impl;

import cn.hutool.json.JSONUtil;
import com.dai.config.ChatAssistantConfig;
import com.dai.config.PersistentChatMemoryStore;
import com.dai.dto.ChatDto;
import com.dai.entity.History;
import com.dai.entity.Title;
import com.dai.vo.TitleVo;
import com.dai.vo.HistoryVo;
import com.dai.mapper.ChatMapper;
import com.dai.service.ChatService;
import com.dai.utils.UserThreadLocal;
import com.dai.vo.UserVo;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.dai.config.TitleAssistantConfig.TitleAssistant;
import com.dai.config.ChatAssistantConfig.ChatAssistant;
import com.dai.config.ChatAssistantConfig.ChatWebAssistant;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatAssistant chatAssistant;

    private final ChatWebAssistant chatWebAssistant;

    private final TitleAssistant titleAssistant;

    private final ChatMapper chatMapper;

    private final PersistentChatMemoryStore chatMemoryStore;

    @Override
    public Flux<String> chatAI(ChatDto chatDto) {
        // 参数校验
        if (chatDto == null || StringUtils.isBlank(chatDto.getMessage())) {
            throw new IllegalArgumentException("Invalid chat request");
        }

        // 异步保存用户消息
        CompletableFuture.runAsync(() ->
                chatMapper.saveHistory(History.builder()
                        .memoryId(chatDto.getMemoryId())
                        .role("user")
                        .content(chatDto.getMessage())
                        .build())
        );

        if (chatDto.isOnlineSearch()) {
            return Flux.create(sink -> {
                StringBuilder responseBuilder = new StringBuilder("<p>");

                chatWebAssistant.chat(chatDto.getMemoryId(), chatDto.getMessage(), LocalDate.now().toString())
                        .onPartialResponse(partial -> {
                            responseBuilder.append(partial);
                            sink.next(partial);
                        })
                        .onCompleteResponse(complete -> {
                            chatMapper.saveHistory(History.builder()
                                    .memoryId(chatDto.getMemoryId())
                                    .role("ai")
                                    .content(responseBuilder + "</p>")
                                    .build());
                            sink.complete();
                        })
                        .onError(sink::error)
                        .start();
            });
        }

        return Flux.create(sink -> {
            StringBuilder responseBuilder = new StringBuilder("<p>");

            chatAssistant.chat(chatDto.getMemoryId(), chatDto.getMessage(), LocalDate.now().toString())
                    .onPartialResponse(partial -> {
                        responseBuilder.append(partial);
                        sink.next(partial);
                    })
                    .onCompleteResponse(complete -> {
                        chatMapper.saveHistory(History.builder()
                                .memoryId(chatDto.getMemoryId())
                                .role("ai")
                                .content(responseBuilder + "</p>")
                                .build());
                        sink.complete();
                    })
                    .onError(sink::error)
                    .start();
        });


    }


    @Override
    public String generateTitle(ChatDto chatDto) {
        String title = titleAssistant.chat(chatDto.getMessage());
        String jsonStr = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(jsonStr, UserVo.class);
        Long id = userVo.getId();
        Title newTitle = Title.builder()
                .memoryId(chatDto.getMemoryId())
                .userId(id)
                .title(title)
                .build();
        chatMapper.updateTitle(newTitle);
        return title;
    }

    @Override
    public List<TitleVo> getHistories() {
        String jsonStr = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(jsonStr, UserVo.class);
        Long id = userVo.getId();
        return chatMapper.getUserTitles(id);
    }

    @Override
    public List<HistoryVo> getHistoryById(Long id) {
        return chatMapper.getHistoryDetails(id);
    }

    @Override
    public void newChat(Long memoryId) {
        History systemHistory = History.builder()
                .memoryId(String.valueOf(memoryId))
                .role("ai") // 修改角色为 system
                .content("您好, 我是你的AI智能助手，有什么我能帮助你的吗？")
                .build();

        Title title = Title.builder()
                .memoryId(String.valueOf(memoryId))
                .title("新对话")
                .build();

        UserVo userVo = JSONUtil.toBean(UserThreadLocal.getSubject(), UserVo.class);
        title.setUserId(userVo.getId());

        chatMapper.saveHistory(systemHistory);
        chatMapper.saveTitle(title);
    }

    @Override
    public void deleteHistory(Long memoryId) {
        chatMapper.deleteHistory(memoryId);
        chatMapper.deleteTitle(memoryId);
        chatMemoryStore.deleteMessages(memoryId);
    }
}
