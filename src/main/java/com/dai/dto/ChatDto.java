package com.dai.dto;

import lombok.Data;

@Data
public class ChatDto {

    private String memoryId;

    private String message;

    private boolean isOnlineSearch;
}
