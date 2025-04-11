package com.dai.controller;

import com.dai.dto.ChatDto;
import com.dai.entity.Result;
import com.dai.service.MedicalService;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/ai/medical")
@RequiredArgsConstructor
public class MedicalController {

    private final MedicalService medicalService;

    @PostMapping()
    public Flux<String> chat(@RequestBody ChatDto chatDto) {
        return medicalService.chat(chatDto);
    }

    @PostMapping("/title")
    public Result generateTitle (@RequestBody ChatDto chatDto) {
        return Result.success(medicalService.generateTitle(chatDto));
    }

    @GetMapping("/history")
    public Result getHistory() {
        List<TitleVo> histories = medicalService.getHistories();
        return Result.success(histories);
    }

    @GetMapping("/history/{id}")
    public Result getHistoryById(@PathVariable Long id) {
        List<HistoryVo> histories = medicalService.getHistoryById(id);
        return Result.success(histories);
    }

    @PostMapping("/new/{id}")
    public Result newHistory(@PathVariable Long id) {
        medicalService.newChat(id);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteHistory(@PathVariable Long id) {
        medicalService.deleteHistory(id);
        return Result.success();
    }
}
