package com.dai.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class History {

    private String memoryId;

    private String role;

    private String content;
}
