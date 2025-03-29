package com.dai.service;

import com.dai.dto.ChatDto;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {

    Flux<String> chatAI(ChatDto dto);

    String generateTitle(ChatDto dto);

    List<TitleVo> getHistories();

    List<HistoryVo> getHistoryById(Long id);

    void newChat(Long memoryId);

    void deleteHistory(Long memoryId);
}
