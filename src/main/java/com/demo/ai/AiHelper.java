package com.demo.ai;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class AiHelper {
    @Autowired
    private AiDashScope properties;
    
    // 存储每个用户的聊天记忆
    private final ConcurrentHashMap<String, ChatMemory> userChatMemories = new ConcurrentHashMap<>();

    public ChatModel getQwenModel() {
        return QwenChatModel.builder()
                .apiKey(properties.getApiKey())
                .modelName(properties.getModelName())
                .enableSearch(true)
                .temperature(0.7f)
                .maxTokens(4096)
                .build();
    }
    
    /**
     * 获取用户的聊天记忆
     * @param userId 用户ID
     * @return ChatMemory实例
     */
    public ChatMemory getUserChatMemory(String userId) {
        return userChatMemories.computeIfAbsent(userId, 
            key -> MessageWindowChatMemory.withMaxMessages(10));
    }
    
    /**
     * 清除用户的聊天记忆
     * @param userId 用户ID
     */
    public void clearUserChatMemory(String userId) {
        userChatMemories.remove(userId);
        System.out.println("已清除用户 {} 的聊天记忆" + userId);;
    }
    
    /**
     * 获取所有活跃用户数量
     * @return 活跃用户数量
     */
    public int getActiveUserCount() {
        return userChatMemories.size();
    }
}


