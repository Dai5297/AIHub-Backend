package com.dai.controller;

import com.dai.dto.ChatDto;
import com.dai.dto.FileDto;
import com.dai.entity.Result;
import com.dai.service.PdfService;
import com.dai.utils.AliOSSUtils;
import com.dai.vo.HistoryVo;
import com.dai.vo.PDFHistoryVo;
import com.dai.vo.TitleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ai/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final AliOSSUtils aliOSSUtils;

    private final PdfService pdfService;

    @PostMapping("/upload")
    public Result upload(FileDto fileDto) {
        pdfService.uploadFile(fileDto);
        return Result.success();
    }

    @PostMapping()
    public Flux<String> chat(@RequestBody ChatDto chatDto) {
        return pdfService.chatAI(chatDto);
    }

    @PostMapping("/title")
    public Result generateTitle (@RequestBody ChatDto chatDto) {
        return Result.success(pdfService.generateTitle(chatDto));
    }

    @GetMapping("/history")
    public Result getHistory() {
        List<TitleVo> histories = pdfService.getHistories();
        return Result.success(histories);
    }

    @GetMapping("/history/{id}")
    public Result getHistoryById(@PathVariable Long id) {
        PDFHistoryVo histories = pdfService.getHistoryById(id);
        return Result.success(histories);
    }

    @PostMapping("/new/{id}")
    public Result newHistory(@PathVariable Long id) {
        pdfService.newChat(id);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteHistory(@PathVariable Long id) {
        pdfService.deleteHistory(id);
        return Result.success();
    }
}
