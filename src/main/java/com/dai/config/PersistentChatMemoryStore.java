package com.dai.config;

import com.dai.mapper.ChatMapper;
import com.dai.mapper.MemoryMapper;
import com.dai.vo.HistoryVo;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取数据库中的对话历史
 */
@Configuration
public class PersistentChatMemoryStore implements ChatMemoryStore {
    @Autowired
    private MemoryMapper memoryMapper;

    @Override
    public List<ChatMessage> getMessages(Object o) {
        String memory = memoryMapper.getMemory(o.toString());
        return ChatMessageDeserializer.messagesFromJson(memory);
    }



    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
        String memory = memoryMapper.getMemory(o.toString());
        if (memory != null){
            memoryMapper.updateMemory(o.toString(),ChatMessageSerializer.messagesToJson(list));
        }else {
            memoryMapper.saveMemory(o.toString(),ChatMessageSerializer.messagesToJson(list));
        }
    }

    @Override
    public void deleteMessages(Object o) {

    }
}
