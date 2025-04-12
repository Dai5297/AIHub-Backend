package com.dai.controller;

import com.dai.dto.ChatDto;
import com.dai.entity.Result;
import com.dai.service.TravelService;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/ai/travel")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    @PostMapping()
    public Flux<String> chat(@RequestBody ChatDto chatDto) {
        return travelService.chatAI(chatDto);
    }

    @PostMapping("/title")
    public Result generateTitle (@RequestBody ChatDto chatDto) {
        return Result.success(travelService.generateTitle(chatDto));
    }

    @GetMapping("/history")
    public Result getHistory() {
        List<TitleVo> histories = travelService.getHistories();
        return Result.success(histories);
    }

    @GetMapping("/history/{id}")
    public Result getHistoryById(@PathVariable Long id) {
        List<HistoryVo> histories = travelService.getHistoryById(id);
        return Result.success(histories);
    }

    @PostMapping("/new/{id}")
    public Result newHistory(@PathVariable Long id) {
        travelService.newChat(id);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteHistory(@PathVariable Long id) {
        travelService.deleteHistory(id);
        return Result.success();
    }
}
