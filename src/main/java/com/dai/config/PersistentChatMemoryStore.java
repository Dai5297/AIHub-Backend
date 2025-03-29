package com.dai.config;

import com.dai.mapper.ChatMapper;
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
    private ChatMapper chatMapper;

    @Override
    public List<ChatMessage> getMessages(Object o) {
        // 转换聊天ID
        Long chatId;
        try {
            chatId = Long.parseLong(String.valueOf(o));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid chat ID: " + o, e);
        }

        List<HistoryVo> historyDetails = chatMapper.getHistoryDetails(chatId);
        List<ChatMessage> messages = new ArrayList<>();
        boolean hasSystemMessage = false;

        // 处理空历史记录情况
        if (historyDetails == null || historyDetails.isEmpty()) {
            messages.add(new SystemMessage("您好, 我是你的AI智能助手，有什么我能帮助你的吗？"));
            return messages;
        }

        // 转换历史记录
        for (HistoryVo detail : historyDetails) {
            if (detail == null) continue;

            String role = detail.getSender();
            String content = detail.getContent();

            if ("system".equals(role)) {
                messages.add(new SystemMessage(content));
                hasSystemMessage = true;
            } else if ("user".equals(role)) {
                messages.add(new UserMessage(content));
            } else if ("ai".equals(role)) {
                messages.add(new AiMessage(content));
            }
        }

        // 强制添加系统消息保障
        if (!hasSystemMessage) {
            messages.add(0, new SystemMessage("您好, 我是你的AI智能助手，有什么我能帮助你的吗？"));
        }

        // 最终空值保障
        if (messages.isEmpty()) {
            messages.add(new SystemMessage("您好, 我是你的AI智能助手，有什么我能帮助你的吗？"));
        }

        return messages;
    }



    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {

    }

    @Override
    public void deleteMessages(Object o) {

    }
}
