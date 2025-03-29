package com.dai.service.impl;

import cn.hutool.json.JSONUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.dai.config.CommonAssistantConfig.CommonAssistant;
import com.dai.config.ChatAssistantConfig.ChatAssistant;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatAssistant chatAssistant;

    @Autowired
    private CommonAssistant commonAssistant;

    @Autowired
    private ChatMapper chatMapper;

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

        // 处理AI响应流
        return Flux.create(sink -> {
            StringBuilder responseBuilder = new StringBuilder("<p>");

            chatAssistant.chat(chatDto.getMemoryId(), chatDto.getMessage())
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
        String title = commonAssistant.chat(chatDto.getMessage());
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
        List<TitleVo> userHistories = chatMapper.getUserTitles(id);
        return userHistories;
    }

    @Override
    public List<HistoryVo> getHistoryById(Long id) {
        List<HistoryVo> historyVoDetails = chatMapper.getHistoryDetails(id);
        return historyVoDetails;
    }

    @Override
    public void newChat(Long memoryId) {
        History systemHistory = History.builder()
                .memoryId(String.valueOf(memoryId))
                .role("system") // 修改角色为 system
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
    }
}
