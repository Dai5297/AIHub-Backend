package com.dai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HistoryVo {

    private String sender;

    private String content;
}
