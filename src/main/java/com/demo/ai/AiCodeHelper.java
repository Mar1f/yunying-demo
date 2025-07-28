package com.demo.ai;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * AI代码助手
 * 基于通义千问的智能对话服务
 */
@Service
@Slf4j
public class AiCodeHelper {
    @Autowired
    private AiDashScope properties;

    public ChatModel getQwenModel() {
        return QwenChatModel.builder()
                .apiKey(properties.getApiKey())
                .modelName(properties.getModelName())
                .enableSearch(true)
                .temperature(0.7f)
                .maxTokens(2048)
                .stops(List.of("Hello"))
                .build();
    }

    private static final String SYSTEM_MESSAGE = 
        "You are a programming assistant helping users with programming learning and job interview questions. " +
        "Your task is to analyze provided code snippets and explain them in simple, easy-to-understand language. " +
        "Break down the code's functionality, purpose, and key components. Use analogies, examples, and plain terms " +
        "to make explanations accessible to people with little coding knowledge. Avoid technical jargon unless " +
        "absolutely necessary, and provide clear explanations for any terms used. The goal is to help readers " +
        "understand what the code does and how it works at a high level.";

    public String chat(String message) {
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_MESSAGE);
        UserMessage userMessage = UserMessage.from(message);
        ChatResponse chatResponse = getQwenModel().chat(systemMessage, userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        System.out.println("AI Output: " + aiMessage.toString());
        return aiMessage.text();
    }

    public String chatWithMessage(UserMessage userMessage) {
        ChatResponse chatResponse = getQwenModel().chat(userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        System.out.println("AI Output: " + aiMessage.toString());
        return aiMessage.text();
    }
}


