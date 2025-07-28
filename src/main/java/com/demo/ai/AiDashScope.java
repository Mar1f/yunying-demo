package com.demo.ai;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @description；
 * @author:mar1
 * @data:2025/07/17
 **/
@Component
@ConfigurationProperties(prefix = "langchain4j.community.dash-scope.chat-model")
public class AiDashScope {

    private String apiKey;
    private String modelName;
    // getter 和 setter
    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
