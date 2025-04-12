package com.dai.service;

import com.dai.dto.ChatDto;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import reactor.core.publisher.Flux;

import java.util.List;

public interface TravelService {
    Flux<String> chatAI(ChatDto chatDto);

    Object generateTitle(ChatDto chatDto);

    List<TitleVo> getHistories();

    List<HistoryVo> getHistoryById(Long id);

    void newChat(Long id);

    void deleteHistory(Long id);
}
