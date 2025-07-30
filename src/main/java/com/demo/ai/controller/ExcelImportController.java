package com.demo.ai.controller;

import com.demo.ai.service.ExcelDataService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import com.demo.ai.model.ProductionData;
import com.demo.ai.model.SalesData;
import com.demo.ai.model.InventoryData;
import com.demo.ai.model.CustomerFeedback;

/**
 * Excel导入控制器
 */
//@Slf4j
@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "*")
public class ExcelImportController {

    private static final Logger log = LoggerFactory.getLogger(ExcelImportController.class);

    @Autowired
    private ExcelDataService excelDataService;

    /**
     * 导入生产数据Excel
     */
    @PostMapping("/import/production")
    public ResponseEntity<Map<String, Object>> importProductionData(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "请选择要上传的文件"
                ));
            }

            String result = excelDataService.importProductionData(file);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", result,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("导入生产数据失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "导入失败：" + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 导入销售数据Excel
     */
    @PostMapping("/import/sales")
    public ResponseEntity<Map<String, Object>> importSalesData(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "请选择要上传的文件"
                ));
            }

            String result = excelDataService.importSalesData(file);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", result,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("导入销售数据失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "导入失败：" + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 导入库存数据Excel
     */
    @PostMapping("/import/inventory")
    public ResponseEntity<Map<String, Object>> importInventoryData(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "请选择要上传的文件"
                ));
            }

            String result = excelDataService.importInventoryData(file);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", result,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("导入库存数据失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "导入失败：" + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 导入客户反馈数据Excel
     */
    @PostMapping("/import/feedback")
    public ResponseEntity<Map<String, Object>> importFeedbackData(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "请选择要上传的文件"
                ));
            }

            String result = excelDataService.importFeedbackData(file);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", result,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("导入客户反馈数据失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "导入失败：" + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 清除所有数据
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearAllData() {
        try {
            excelDataService.clearAllData();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "已清除所有数据",
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("清除数据失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "清除失败：" + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 获取数据统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDataStats() {
        try {
            Map<String, Object> stats = excelDataService.getDataStats();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", stats,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            log.error("获取数据统计失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "获取统计失败：" + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }
} 