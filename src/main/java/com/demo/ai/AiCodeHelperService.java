package com.demo.ai;

import dev.langchain4j.service.SystemMessage;

/**
 * @description；
 * @author:mar1
 * @data:2025/07/18
 **/
//@AiService
public interface AiCodeHelperService {

    @SystemMessage(fromResource = "ai-System-prompt.txt")
    String chat(String userMessage);
}

