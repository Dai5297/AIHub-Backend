package com.dai.mapper;

import com.dai.entity.Title;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PdfMapper {
    void saveFile(@Param("title") Title title, @Param("fileName") String fileName);
}
