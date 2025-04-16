package com.dai.service.impl;

import cn.hutool.json.JSONUtil;
import com.dai.entity.Title;
import com.dai.mapper.PdfMapper;
import com.dai.service.PdfService;
import com.dai.utils.AliOSSUtils;
import com.dai.utils.UserThreadLocal;
import com.dai.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final PdfMapper pdfMapper;

    private final AliOSSUtils ossUtils;

    @Override
    public void uploadFile(MultipartFile file, String memoryId) {
        String fileName;
        try {
            fileName = ossUtils.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String subject = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(subject, UserVo.class);
        Title title = Title.builder()
                .memoryId(memoryId)
                .title(file.getName())
                .userId(userVo.getId())
                .build();
        pdfMapper.saveFile(title, fileName);
    }
}
