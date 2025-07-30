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
            if (question == null || question.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "问题不能为空"));
            }

            String analysisResult = intelligentAnalysisService.intelligentAnalysis(question);
            
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
            if (question == null || question.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "问题不能为空"));
            }

            Map<String, Object> analysisResult = intelligentAnalysisService.intelligentAnalysisWithCharts(question);
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

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Intelligent Dashboard",
            "timestamp", System.currentTimeMillis()
        ));
    }
} 