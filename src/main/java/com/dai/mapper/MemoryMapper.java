package com.dai.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemoryMapper {

    String getMemory(@Param("memoryId") String memoryId);

    void updateMemory(@Param("sessionId") String sessionId, @Param("content") String content);

    void saveMemory(@Param("sessionId") String sessionId, @Param("content") String content);
}
