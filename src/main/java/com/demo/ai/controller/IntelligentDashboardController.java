package com.demo.ai.controller;

import com.demo.ai.service.IntelligentAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 智能运营看板控制器
 */
@RestController
@RequestMapping("/api/dashboard")
//@Slf4j
@CrossOrigin(origins = "*")
public class IntelligentDashboardController {

    private static final Logger log = LoggerFactory.getLogger(IntelligentDashboardController.class);

    @Autowired
    private IntelligentAnalysisService intelligentAnalysisService;

    /**
     * 智能对话分析
     */
    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> intelligentAnalysis(@RequestBody Map<String, String> request) {
        try {
            String question = request.get("question");
            String userId = request.getOrDefault("userId", "default_user");
            
            if (question == null || question.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "问题不能为空"));
            }

            String analysisResult = intelligentAnalysisService.intelligentAnalysis(question, userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "analysis", analysisResult,
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("智能分析失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "分析过程中出现错误",
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 智能对话分析（包含图表）
     */
    @PostMapping("/analyze-with-charts")
    public ResponseEntity<Map<String, Object>> intelligentAnalysisWithCharts(@RequestBody Map<String, String> request) {
        try {
            String question = request.get("question");
            String userId = request.getOrDefault("userId", "default_user");
            
            if (question == null || question.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "问题不能为空"));
            }

            Map<String, Object> analysisResult = intelligentAnalysisService.intelligentAnalysisWithCharts(question, userId);
            analysisResult.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(analysisResult);
        } catch (Exception e) {
            log.error("智能分析失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "分析过程中出现错误",
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 清除用户聊天记忆
     */
    @PostMapping("/clear-memory")
    public ResponseEntity<Map<String, Object>> clearUserMemory(@RequestBody Map<String, String> request) {
        try {
            String userId = request.getOrDefault("userId", "default_user");
            intelligentAnalysisService.clearUserMemory(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "聊天记忆已清除",
                "userId", userId,
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("清除聊天记忆失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "清除记忆过程中出现错误",
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 获取用户聊天记忆信息
     */
    @GetMapping("/memory-info/{userId}")
    public ResponseEntity<Map<String, Object>> getUserMemoryInfo(@PathVariable String userId) {
        try {
            Map<String, Object> memoryInfo = intelligentAnalysisService.getUserMemoryInfo(userId);
            memoryInfo.put("success", true);
            memoryInfo.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(memoryInfo);
        } catch (Exception e) {
            log.error("获取记忆信息失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "获取记忆信息过程中出现错误",
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 获取实时数据看板
     */
    @GetMapping("/realtime")
    public ResponseEntity<Map<String, Object>> getRealTimeDashboard() {
        try {
            Map<String, Object> dashboard = intelligentAnalysisService.getRealTimeDashboard();
            dashboard.put("success", true);
            dashboard.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("获取实时看板失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", "获取数据失败",
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Intelligent Dashboard",
            "timestamp", System.currentTimeMillis()
        ));
    }
} 