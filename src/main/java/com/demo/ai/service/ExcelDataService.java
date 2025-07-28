package com.demo.ai.service;

import com.demo.ai.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Excel数据管理服务
 */
@Service
//@Slf4j
public class ExcelDataService {

    private static final Logger log = LoggerFactory.getLogger(ExcelDataService.class);

    // 内存存储数据
    private final Map<String, List<ProductionData>> productionDataMap = new ConcurrentHashMap<>();
    private final Map<String, List<SalesData>> salesDataMap = new ConcurrentHashMap<>();
    private final Map<String, List<InventoryData>> inventoryDataMap = new ConcurrentHashMap<>();
    private final Map<String, List<CustomerFeedback>> feedbackDataMap = new ConcurrentHashMap<>();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 导入生产数据Excel
     */
    public String importProductionData(MultipartFile file) {
        try {
            log.info("开始导入生产数据文件: {}", file.getOriginalFilename());
            
            if (file.isEmpty()) {
                log.warn("上传的文件为空");
                return "上传的文件为空";
            }
            
            try (InputStream is = file.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                
                Sheet sheet = workbook.getSheetAt(0);
                List<ProductionData> dataList = new ArrayList<>();
                
                log.info("Excel文件包含 {} 行数据", sheet.getLastRowNum() + 1);
                
                // 跳过表头，从第二行开始读取
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        log.debug("跳过空行: {}", i + 1);
                        continue;
                    }
                    
                    try {
                        ProductionData data = new ProductionData();
                        data.setId((long) i);
                        data.setProductName(getCellValueAsString(row.getCell(0)));
                        data.setProductionQuantity(getCellValueAsInt(row.getCell(1)));
                        data.setDefectQuantity(getCellValueAsInt(row.getCell(2)));
                        data.setProductionDate(parseDateTime(getCellValueAsString(row.getCell(3))));
                        data.setProductionLine(getCellValueAsString(row.getCell(4)));
                        data.setCostPerUnit(getCellValueAsDouble(row.getCell(5)));
                        data.setEfficiencyRate(getCellValueAsDouble(row.getCell(6)));
                        
                        dataList.add(data);
                        log.debug("成功解析第 {} 行数据: {}", i + 1, data.getProductName());
                    } catch (Exception e) {
                        log.warn("跳过无效行 {}: {}", i + 1, e.getMessage());
                    }
                }
                
                String key = "production_" + System.currentTimeMillis();
                productionDataMap.put(key, dataList);
                
                log.info("成功导入生产数据 {} 条", dataList.size());
                return "成功导入生产数据 " + dataList.size() + " 条";
                
            }
        } catch (IOException e) {
            log.error("导入生产数据失败", e);
            return "导入失败：" + e.getMessage();
        } catch (Exception e) {
            log.error("导入生产数据时发生未知错误", e);
            return "导入失败：" + e.getMessage();
        }
    }

    /**
     * 导入销售数据Excel
     */
    public String importSalesData(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            List<SalesData> dataList = new ArrayList<>();
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    SalesData data = new SalesData();
                    data.setId((long) i);
                    data.setProductName(getCellValueAsString(row.getCell(0)));
                    data.setSalesQuantity(getCellValueAsInt(row.getCell(1)));
                    data.setSalesAmount(getCellValueAsDouble(row.getCell(2)));
                    data.setSalesDate(parseDateTime(getCellValueAsString(row.getCell(3))));
                    data.setCustomerId(getCellValueAsString(row.getCell(4)));
                    data.setRegion(getCellValueAsString(row.getCell(5)));
                    data.setSalesChannel(getCellValueAsString(row.getCell(6)));
                    data.setProfitMargin(getCellValueAsDouble(row.getCell(7)));
                    
                    dataList.add(data);
                } catch (Exception e) {
                    log.warn("跳过无效行 {}: {}", i + 1, e.getMessage());
                }
            }
            
            String key = "sales_" + System.currentTimeMillis();
            salesDataMap.put(key, dataList);
            
            log.info("成功导入销售数据 {} 条", dataList.size());
            return "成功导入销售数据 " + dataList.size() + " 条";
            
        } catch (IOException e) {
            log.error("导入销售数据失败", e);
            return "导入失败：" + e.getMessage();
        }
    }

    /**
     * 导入库存数据Excel
     */
    public String importInventoryData(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            List<InventoryData> dataList = new ArrayList<>();
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    InventoryData data = new InventoryData();
                    data.setId((long) i);
                    data.setProductName(getCellValueAsString(row.getCell(0)));
                    data.setCurrentStock(getCellValueAsInt(row.getCell(1)));
                    data.setMinStockLevel(getCellValueAsInt(row.getCell(2)));
                    data.setMaxStockLevel(getCellValueAsInt(row.getCell(3)));
                    data.setWarehouseLocation(getCellValueAsString(row.getCell(4)));
                    data.setUnitCost(getCellValueAsDouble(row.getCell(5)));
                    data.setSupplierId(getCellValueAsString(row.getCell(6)));
                    
                    dataList.add(data);
                } catch (Exception e) {
                    log.warn("跳过无效行 {}: {}", i + 1, e.getMessage());
                }
            }
            
            String key = "inventory_" + System.currentTimeMillis();
            inventoryDataMap.put(key, dataList);
            
            log.info("成功导入库存数据 {} 条", dataList.size());
            return "成功导入库存数据 " + dataList.size() + " 条";
            
        } catch (IOException e) {
            log.error("导入库存数据失败", e);
            return "导入失败：" + e.getMessage();
        }
    }

    /**
     * 导入客户反馈数据Excel
     */
    public String importFeedbackData(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            List<CustomerFeedback> dataList = new ArrayList<>();
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    CustomerFeedback data = new CustomerFeedback();
                    data.setId((long) i);
                    data.setCustomerId(getCellValueAsString(row.getCell(0)));
                    data.setProductName(getCellValueAsString(row.getCell(1)));
                    data.setFeedbackType(getCellValueAsString(row.getCell(2)));
                    data.setFeedbackContent(getCellValueAsString(row.getCell(3)));
                    data.setSatisfactionScore(getCellValueAsInt(row.getCell(4)));
                    data.setFeedbackDate(parseDateTime(getCellValueAsString(row.getCell(5))));
                    data.setStatus(getCellValueAsString(row.getCell(6)));
                    data.setPriority(getCellValueAsString(row.getCell(7)));
                    
                    dataList.add(data);
                } catch (Exception e) {
                    log.warn("跳过无效行 {}: {}", i + 1, e.getMessage());
                }
            }
            
            String key = "feedback_" + System.currentTimeMillis();
            feedbackDataMap.put(key, dataList);
            
            log.info("成功导入客户反馈数据 {} 条", dataList.size());
            return "成功导入客户反馈数据 " + dataList.size() + " 条";
            
        } catch (IOException e) {
            log.error("导入客户反馈数据失败", e);
            return "导入失败：" + e.getMessage();
        }
    }

    /**
     * 获取所有生产数据
     */
    public List<ProductionData> getAllProductionData() {
        List<ProductionData> allData = new ArrayList<>();
        productionDataMap.values().forEach(allData::addAll);
        return allData;
    }

    /**
     * 获取所有销售数据
     */
    public List<SalesData> getAllSalesData() {
        List<SalesData> allData = new ArrayList<>();
        salesDataMap.values().forEach(allData::addAll);
        return allData;
    }

    /**
     * 获取所有库存数据
     */
    public List<InventoryData> getAllInventoryData() {
        List<InventoryData> allData = new ArrayList<>();
        inventoryDataMap.values().forEach(allData::addAll);
        return allData;
    }

    /**
     * 获取所有客户反馈数据
     */
    public List<CustomerFeedback> getAllFeedbackData() {
        List<CustomerFeedback> allData = new ArrayList<>();
        feedbackDataMap.values().forEach(allData::addAll);
        return allData;
    }

    /**
     * 根据日期范围获取生产数据
     */
    public List<ProductionData> getProductionDataByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return getAllProductionData().stream()
                .filter(data -> data.getProductionDate() != null &&
                        !data.getProductionDate().isBefore(startDate) &&
                        !data.getProductionDate().isAfter(endDate))
                .toList();
    }

    /**
     * 根据日期范围获取销售数据
     */
    public List<SalesData> getSalesDataByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return getAllSalesData().stream()
                .filter(data -> data.getSalesDate() != null &&
                        !data.getSalesDate().isBefore(startDate) &&
                        !data.getSalesDate().isAfter(endDate))
                .toList();
    }

    /**
     * 根据日期范围获取客户反馈数据
     */
    public List<CustomerFeedback> getFeedbackDataByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return getAllFeedbackData().stream()
                .filter(data -> data.getFeedbackDate() != null &&
                        !data.getFeedbackDate().isBefore(startDate) &&
                        !data.getFeedbackDate().isAfter(endDate))
                .toList();
    }

    /**
     * 清除所有数据
     */
    public void clearAllData() {
        productionDataMap.clear();
        salesDataMap.clear();
        inventoryDataMap.clear();
        feedbackDataMap.clear();
        log.info("已清除所有数据");
    }

    // 辅助方法
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    private Integer getCellValueAsInt(Cell cell) {
        if (cell == null) return 0;
        switch (cell.getCellType()) {
            case NUMERIC: return (int) cell.getNumericCellValue();
            case STRING: 
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default: return 0;
        }
    }

    private Double getCellValueAsDouble(Cell cell) {
        if (cell == null) return 0.0;
        switch (cell.getCellType()) {
            case NUMERIC: return cell.getNumericCellValue();
            case STRING: 
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default: return 0.0;
        }
    }

    private LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return LocalDateTime.now();
        }
        
        // 支持的日期格式
        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/M/d HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        };
        
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (Exception e) {
                // 继续尝试下一个格式
            }
        }
        
        log.warn("无法解析日期: {}, 使用当前时间", dateStr);
        return LocalDateTime.now();
    }
} 