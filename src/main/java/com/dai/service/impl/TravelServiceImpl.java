package com.dai.service.impl;

import cn.hutool.json.JSONUtil;
import com.dai.config.PersistentChatMemoryStore;
import com.dai.config.TitleAssistantConfig.TitleAssistant;
import com.dai.config.TravelAssistantConfig.TravelAssistant;
import com.dai.dto.ChatDto;
import com.dai.entity.History;
import com.dai.entity.Title;
import com.dai.mapper.TravelMapper;
import com.dai.service.TravelService;
import com.dai.utils.UserThreadLocal;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import com.dai.vo.UserVo;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

    private final TravelMapper travelMapper;

    private final TravelAssistant travelAssistant;

    private final TitleAssistant titleAssistant;

    private final PersistentChatMemoryStore chatMemoryStore;

    @Override
    public Flux<String> chatAI(ChatDto chatDto) {
        // 参数校验
        if (chatDto == null || StringUtils.isBlank(chatDto.getMessage())) {
            throw new IllegalArgumentException("Invalid chat request");
        }

        // 异步保存用户消息
        CompletableFuture.runAsync(() ->
                travelMapper.saveHistory(History.builder()
                        .memoryId(chatDto.getMemoryId())
                        .role("user")
                        .content(chatDto.getMessage())
                        .build())
        );

        return Flux.create(sink -> {
            StringBuilder responseBuilder = new StringBuilder("<p>");

            travelAssistant.chat(chatDto.getMemoryId(), chatDto.getMessage(), LocalDate.now().toString())
                    .onPartialResponse(partial -> {
                        responseBuilder.append(partial);
                        sink.next(partial);
                    })
                    .onCompleteResponse(complete -> {
                        travelMapper.saveHistory(History.builder()
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
    public Object generateTitle(ChatDto chatDto) {
        String title = titleAssistant.chat(chatDto.getMessage());
        String jsonStr = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(jsonStr, UserVo.class);
        Long id = userVo.getId();
        Title newTitle = Title.builder()
                .memoryId(chatDto.getMemoryId())
                .userId(id)
                .title(title)
                .build();
        travelMapper.updateTitle(newTitle);
        return title;
    }

    @Override
    public List<TitleVo> getHistories() {
        String jsonStr = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(jsonStr, UserVo.class);
        Long id = userVo.getId();
        return travelMapper.getUserTitles(id);
    }

    @Override
    public List<HistoryVo> getHistoryById(Long id) {
        return travelMapper.getHistoryDetails(id);
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

        travelMapper.saveHistory(systemHistory);
        travelMapper.saveTitle(title);
    }

    @Override
    public void deleteHistory(Long memoryId) {
        travelMapper.deleteHistory(memoryId);
        travelMapper.deleteTitle(memoryId);
        chatMemoryStore.deleteMessages(memoryId);
    }
}
