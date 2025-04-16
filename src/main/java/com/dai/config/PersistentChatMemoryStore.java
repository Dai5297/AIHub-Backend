package com.dai.config;

import com.dai.constant.SystemMessages;
import com.dai.mapper.MemoryMapper;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 获取数据库中的对话历史
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PersistentChatMemoryStore implements ChatMemoryStore {

    private final MemoryMapper memoryMapper;

    @Override
    public List<ChatMessage> getMessages(Object o) {
        String memory = memoryMapper.getMemory(o.toString());
        return ChatMessageDeserializer.messagesFromJson(memory);
    }


    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
        String memory = memoryMapper.getMemory(o.toString());
        ChatMessage first = list.getFirst();
        if (first.type() != ChatMessageType.SYSTEM && first.type() != ChatMessageType.USER) {
            String memoryType = memoryMapper.getMemoryType(o.toString());
            switch (memoryType) {
                case "chat_title":
                    list.addFirst(new SystemMessage(SystemMessages.CHAT_SYSTEM_MESSAGE));
                    break;
                case "medical_title":
                    list.addFirst(new SystemMessage(SystemMessages.MEDICAL_SYSTEM_MESSAGE));
                    break;
                case "travel_title":
                    list.addFirst(new SystemMessage(SystemMessages.TRAVEL_SYSTEM_MESSAGE));
                    break;
                case "pdf_title":
                    list.addFirst(new SystemMessage(SystemMessages.PDF_SYSTEM_MESSAGE));
                    break;
            }
        }
        if (memory != null) {
            memoryMapper.updateMemory(o.toString(), ChatMessageSerializer.messagesToJson(list));
        } else {
            memoryMapper.saveMemory(o.toString(), ChatMessageSerializer.messagesToJson(list));
        }
    }

    @Override
    public void deleteMessages(Object o) {
        memoryMapper.deleteMemory(o.toString());
    }
}
