package com.dai.service;

import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
    void uploadFile(MultipartFile file, String memoryId);
}
