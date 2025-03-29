package com.dai.controller;

import com.dai.dto.ChatDto;
import com.dai.vo.HistoryVo;
import com.dai.entity.Result;
import com.dai.vo.TitleVo;
import com.dai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/ai/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping()
    public Flux<String> chat(@RequestBody ChatDto chatDto) {
        return chatService.chatAI(chatDto);
    }

    @PostMapping("/title")
    public Result generateTitle (@RequestBody ChatDto chatDto) {
        return Result.success(chatService.generateTitle(chatDto));
    }

    @GetMapping("/history")
    public Result getHistory() {
        List<TitleVo> histories = chatService.getHistories();
        return Result.success(histories);
    }

    @GetMapping("/history/{id}")
    public Result getHistoryById(@PathVariable Long id) {
        List<HistoryVo> histories = chatService.getHistoryById(id);
        return Result.success(histories);
    }

    @PostMapping("/new/{id}")
    public Result newHistory(@PathVariable Long id) {
        chatService.newChat(id);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteHistory(@PathVariable Long id) {
        chatService.deleteHistory(id);
        return Result.success();
    }
}
