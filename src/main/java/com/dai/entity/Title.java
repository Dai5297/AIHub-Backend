package com.dai.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Title {

    private Long id;

    private Long userId;

    private String memoryId;

    private String title;
}
