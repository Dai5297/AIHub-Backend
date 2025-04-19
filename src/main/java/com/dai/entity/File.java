package com.dai.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {

    private Long userId;

    private String memoryId;

    private String fileName;

    private String url;
}
