package com.dai.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileDto {

    private MultipartFile file;

    private String fileName;

    private String memoryId;
}
