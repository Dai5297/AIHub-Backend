package com.dai.controller;

import com.dai.entity.Result;
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

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        // 验证文件非空
        if (file.isEmpty()) {
            return Result.error();
        }

        String upload;

        try {
            upload = aliOSSUtils.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(upload);
    }
}
