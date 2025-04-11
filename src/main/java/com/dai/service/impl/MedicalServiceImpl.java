package com.dai.service.impl;

import cn.hutool.json.JSONUtil;
import com.dai.config.TitleAssistantConfig.TitleAssistant;
import com.dai.config.MedicalAssistantConfig.MedicalAssistant;
import com.dai.config.PersistentChatMemoryStore;
import com.dai.dto.ChatDto;
import com.dai.entity.History;
import com.dai.entity.Title;
import com.dai.mapper.MedicalMapper;
import com.dai.service.MedicalService;
import com.dai.utils.UserThreadLocal;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import com.dai.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class MedicalServiceImpl implements MedicalService {

    @Autowired
    private MedicalAssistant medicalAssistant;

    @Autowired
    private TitleAssistant titleAssistant;

    @Autowired
    private MedicalMapper medicalMapper;

    @Autowired
    private PersistentChatMemoryStore chatMemoryStore;

    @Override
    public Flux<String> chat(ChatDto chatDto) {

        medicalMapper.saveHistory(History.builder()
                .role("user")
                .content(chatDto.getMessage())
                .memoryId(chatDto.getMemoryId())
                .build()
        );

        return Flux.create(sink -> {
            StringBuilder responseBuilder = new StringBuilder("<p>");
            medicalAssistant.chat(chatDto.getMemoryId(), chatDto.getMessage())
                    .onPartialResponse(partial -> {
                        responseBuilder.append(partial);
                        sink.next(partial);
                    })
                    .onCompleteResponse(complete -> {
                        medicalMapper.saveHistory(History.builder()
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
        medicalMapper.updateTitle(Title.builder()
                .userId(id)
                .title(title)
                .memoryId(chatDto.getMemoryId())
                .build()
        );
        return title;
    }

    @Override
    public List<TitleVo> getHistories() {
        String jsonStr = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(jsonStr, UserVo.class);
        Long id = userVo.getId();
        List<TitleVo> userHistories = medicalMapper.getUserTitles(id);
        return userHistories;
    }

    @Override
    public List<HistoryVo> getHistoryById(Long id) {
        List<HistoryVo> historyVoDetails = medicalMapper.getHistoryDetails(id);
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

        medicalMapper.saveHistory(systemHistory);
        medicalMapper.saveTitle(title);
    }

    @Override
    public void deleteHistory(Long memoryId) {
        medicalMapper.deleteHistory(memoryId);
        medicalMapper.deleteTitle(memoryId);
        chatMemoryStore.deleteMessages(memoryId);
    }
}
