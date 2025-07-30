# AI对话记忆功能说明

## 功能概述

本项目已成功集成了LangChain4j的ChatMemory功能，使AI对话具有了会话记忆能力。现在AI可以记住之前的对话内容，提供更连贯和个性化的对话体验。

## 主要特性

### 1. 会话记忆
- **用户隔离**：每个用户都有独立的聊天记忆
- **消息窗口**：每个用户最多保存10条消息的历史记录
- **自动管理**：超出限制的消息会自动移除

### 2. 记忆管理
- **清除记忆**：用户可以随时清除自己的聊天记忆
- **记忆信息**：可以查看当前记忆中的消息数量
- **持久化**：用户ID会保存在浏览器本地存储中

### 3. 智能对话
- **上下文理解**：AI能够理解之前的对话内容
- **连续对话**：支持多轮对话，AI会记住之前的问答
- **个性化回复**：基于历史对话提供更相关的回复

## 技术实现

### 后端实现

#### 1. AiHelper类增强
```java
@Service
public class AiHelper {
    // 存储每个用户的聊天记忆
    private final ConcurrentHashMap<String, ChatMemory> userChatMemories = new ConcurrentHashMap<>();
    
    // 获取用户的聊天记忆
    public ChatMemory getUserChatMemory(String userId) {
        return userChatMemories.computeIfAbsent(userId, 
            key -> MessageWindowChatMemory.withMaxMessages(10));
    }
    
    // 清除用户的聊天记忆
    public void clearUserChatMemory(String userId) {
        userChatMemories.remove(userId);
    }
}
```

#### 2. IntelligentAnalysisService增强
```java
@Service
public class IntelligentAnalysisService {
    // 带记忆的AI分析
    public String intelligentAnalysis(String userQuestion, String userId) {
        // 使用ChatMemory进行对话
        String analysisResult = callAiModelWithMemory(analysisPrompt, userId);
        return analysisResult;
    }
    
    // 带记忆的AI调用
    private String callAiModelWithMemory(String prompt, String userId) {
        ChatMemory chatMemory = aiHelper.getUserChatMemory(userId);
        
        // 添加用户消息到记忆
        chatMemory.add(UserMessage.from(prompt));
        
        // 获取所有历史消息
        List<ChatMessage> messages = chatMemory.messages();
        
        // 调用AI模型
        ChatResponse chatResponse = aiHelper.getQwenModel().chat(messages);
        AiMessage aiMessage = chatResponse.aiMessage();
        
        // 添加AI回复到记忆
        chatMemory.add(aiMessage);
        
        return aiMessage.text();
    }
}
```

#### 3. 新增API接口
- `POST /api/dashboard/analyze` - 智能分析（带记忆）
- `POST /api/dashboard/analyze-with-charts` - 智能分析（带图表和记忆）
- `POST /api/dashboard/clear-memory` - 清除用户记忆
- `GET /api/dashboard/memory-info/{userId}` - 获取记忆信息
- `POST /api/dashboard/test-memory` - 测试记忆功能

### 前端实现

#### 1. 用户ID管理
```javascript
// 生成用户ID
function generateUserId() {
    let userId = localStorage.getItem('ai_user_id');
    if (!userId) {
        userId = 'user_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
        localStorage.setItem('ai_user_id', userId);
    }
    return userId;
}
```

#### 2. 记忆功能集成
```javascript
// 发送聊天消息（带记忆功能）
async function sendChatMessage() {
    const response = await fetch('/api/dashboard/analyze-with-charts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            question: message,
            userId: currentUserId
        })
    });
}

// 清除聊天记忆
async function clearChatMemory() {
    const response = await fetch('/api/dashboard/clear-memory', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: currentUserId })
    });
}
```

## 使用方法

### 1. 基本对话
1. 打开系统，系统会自动生成用户ID
2. 在AI对话区域输入问题
3. AI会记住之前的对话内容，提供连贯的回复

### 2. 清除记忆
1. 点击AI对话区域右上角的"清除记忆"按钮
2. 确认清除后，之前的对话历史将被清空
3. 可以开始新的对话会话

### 3. 查看记忆信息
1. 打开浏览器开发者工具
2. 在控制台输入：`getMemoryInfo()`
3. 查看当前记忆中的消息数量

## 配置说明

### 记忆配置
在 `application.yml` 中可以配置记忆相关参数：
```yaml
app:
  dashboard:
    max-chat-history: 50     # 最大聊天历史记录数（前端显示）
```

### 记忆窗口大小
在 `AiHelper.java` 中可以调整记忆窗口大小：
```java
MessageWindowChatMemory.withMaxMessages(10)  // 每个用户最多保存10条消息
```

## 注意事项

1. **内存管理**：用户记忆存储在服务器内存中，重启后会被清空
2. **用户隔离**：不同用户的记忆完全隔离，互不影响
3. **性能考虑**：大量用户同时使用时需要注意内存占用
4. **数据持久化**：当前实现为内存存储，如需持久化需要添加数据库支持

## 扩展建议

1. **数据库持久化**：将用户记忆保存到数据库中
2. **记忆压缩**：对长期记忆进行压缩和摘要
3. **记忆分类**：按主题或时间对记忆进行分类
4. **记忆搜索**：支持在历史记忆中搜索相关内容
5. **多设备同步**：支持用户在不同设备间同步记忆

## 测试验证

### 1. 功能测试
1. 发送第一条消息："你好，我叫张三"
2. 发送第二条消息："你还记得我的名字吗？"
3. 验证AI是否记得用户的名字

### 2. 记忆清除测试
1. 进行几轮对话
2. 点击"清除记忆"按钮
3. 再次询问之前的信息
4. 验证AI是否已忘记之前的内容

### 3. 用户隔离测试
1. 使用不同浏览器或清除本地存储
2. 验证不同用户是否有独立的记忆

通过以上修改，项目现在具备了完整的AI对话记忆功能，可以提供更智能和个性化的对话体验。