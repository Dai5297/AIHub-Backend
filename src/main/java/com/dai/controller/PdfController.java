package com.dai.controller;

import com.dai.entity.Result;
import com.dai.service.PdfService;
import com.dai.utils.AliOSSUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ai/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final AliOSSUtils aliOSSUtils;

    private final PdfService pdfService;

    @PostMapping("/upload")
    public Result upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("memoryId") String memoryId
    ) {
        pdfService.uploadFile(file, memoryId);
        return Result.success();
    }
}
