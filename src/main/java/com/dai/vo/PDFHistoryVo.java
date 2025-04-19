package com.dai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PDFHistoryVo {

    String fileName;

    List<HistoryVo> historyVos;
}
