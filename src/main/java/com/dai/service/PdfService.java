package com.dai.service;

import com.dai.dto.ChatDto;
import com.dai.dto.FileDto;
import com.dai.vo.PDFHistoryVo;
import com.dai.vo.TitleVo;
import reactor.core.publisher.Flux;

import java.util.List;

public interface PdfService {
    void uploadFile(FileDto fileDto);

    Flux<String> chatAI(ChatDto chatDto);

    String generateTitle(ChatDto dto);

    List<TitleVo> getHistories();

    PDFHistoryVo getHistoryById(Long id);

    void newChat(Long memoryId);

    void deleteHistory(Long memoryId);
}
