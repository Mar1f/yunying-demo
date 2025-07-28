package com.demo.ai.service;

import com.demo.ai.AiCodeHelper;
import com.demo.ai.model.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Intelligent Analysis Service
 * Integrates AI conversation and data analysis functionality
 */
@Service
@Slf4j
public class IntelligentAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(IntelligentAnalysisService.class);

    @Autowired
    private AiCodeHelper aiCodeHelper;
    
    @Autowired
    private ExcelDataService excelDataService;

    private static final String SYSTEM_PROMPT = 
        "You are a professional automotive industry operations data analyst. You need to analyze production, sales, " +
        "inventory, customer feedback and other multi-dimensional data based on user questions, and provide " +
        "professional analysis reports and suggestions. Please explain the meaning behind the data in simple and " +
        "easy-to-understand language, and provide specific improvement suggestions.\n\n" +
        "You can analyze the following data dimensions:\n" +
        "1. Production data: production efficiency, defect rate, cost control, production line performance\n" +
        "2. Sales data: sales revenue, profit margin, regional distribution, sales channels, customer analysis\n" +
        "3. Inventory data: inventory levels, turnover rate, stockout risk, warehouse management\n" +
        "4. Customer feedback: satisfaction, complaint handling, product improvement suggestions, service quality\n\n" +
        "Please provide data-driven analysis reports based on users' specific questions. " +
        "If no relevant data is available, please inform the user and suggest what data should be uploaded.";

    /**
     * Intelligent conversation analysis
     */
    public String intelligentAnalysis(String userQuestion) {
        try {
            log.info("开始AI分析，用户问题: {}", userQuestion);
            
            // Get relevant data
            Map<String, Object> dataContext = getDataContext(userQuestion);
            
            // Check if we have any data
            if (dataContext.isEmpty()) {
                return "抱歉，目前没有找到相关的数据。请先上传相应的Excel数据文件，然后重新提问。\n\n" +
                       "您可以上传以下类型的数据：\n" +
                       "1. 生产数据 - 包含产品名称、生产数量、缺陷数量等信息\n" +
                       "2. 销售数据 - 包含销售数量、销售金额、客户信息等\n" +
                       "3. 库存数据 - 包含当前库存、最低库存、仓库位置等\n" +
                       "4. 客户反馈数据 - 包含反馈类型、满意度评分、处理状态等";
            }
            
            // Build analysis prompt
            String analysisPrompt = buildAnalysisPrompt(userQuestion, dataContext);
            
            log.info("构建分析提示，数据上下文包含 {} 个维度", dataContext.size());
            
            // Call AI for analysis
            String analysisResult = aiCodeHelper.chat(analysisPrompt);
            
            log.info("AI分析完成");
            return analysisResult;
        } catch (Exception e) {
            log.error("Intelligent analysis failed", e);
            return "抱歉，分析过程中发生错误。请检查数据格式是否正确，或稍后重试。";
        }
    }

    /**
     * Intelligent conversation analysis with chart data
     */
    public Map<String, Object> intelligentAnalysisWithCharts(String userQuestion) {
        try {
            log.info("开始AI分析，用户问题: {}", userQuestion);
            
            // Get relevant data
            Map<String, Object> dataContext = getDataContext(userQuestion);
            
            // Check if we have any data
            if (dataContext.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "抱歉，目前没有找到相关的数据。请先上传相应的Excel数据文件，然后重新提问。\n\n" +
                       "您可以上传以下类型的数据：\n" +
                       "1. 生产数据 - 包含产品名称、生产数量、缺陷数量等信息\n" +
                       "2. 销售数据 - 包含销售数量、销售金额、客户信息等\n" +
                       "3. 库存数据 - 包含当前库存、最低库存、仓库位置等\n" +
                       "4. 客户反馈数据 - 包含反馈类型、满意度评分、处理状态等");
                return response;
            }
            
            // Generate chart data based on question
            Map<String, Object> chartData = generateChartData(userQuestion, dataContext);
            
            // Build analysis prompt
            String analysisPrompt = buildAnalysisPrompt(userQuestion, dataContext);
            
            log.info("构建分析提示，数据上下文包含 {} 个维度", dataContext.size());
            
            // Call AI for analysis
            String analysisResult = aiCodeHelper.chat(analysisPrompt);
            
            log.info("AI分析完成");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("analysis", analysisResult);
            response.put("charts", chartData);
            response.put("dataContext", dataContext);
            
            return response;
        } catch (Exception e) {
            log.error("Intelligent analysis failed", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "抱歉，分析过程中发生错误。请检查数据格式是否正确，或稍后重试。");
            return response;
        }
    }

    /**
     * Get data context
     */
    private Map<String, Object> getDataContext(String question) {
        Map<String, Object> context = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);
        LocalDateTime threeMonthsAgo = now.minusMonths(3);

        // Get all available data first
        List<ProductionData> allProductionData = excelDataService.getAllProductionData();
        List<SalesData> allSalesData = excelDataService.getAllSalesData();
        List<InventoryData> allInventoryData = excelDataService.getAllInventoryData();
        List<CustomerFeedback> allFeedbackData = excelDataService.getAllFeedbackData();
        
        log.info("当前数据状态 - 生产数据: {} 条, 销售数据: {} 条, 库存数据: {} 条, 反馈数据: {} 条", 
                allProductionData.size(), allSalesData.size(), allInventoryData.size(), allFeedbackData.size());

        // Get relevant data based on question keywords (case insensitive)
        String lowerQuestion = question.toLowerCase();
        
        if (lowerQuestion.contains("生产") || lowerQuestion.contains("效率") || lowerQuestion.contains("缺陷") || 
            lowerQuestion.contains("production") || lowerQuestion.contains("efficiency") || lowerQuestion.contains("defect")) {
            context.put("productionData", getProductionSummary(oneMonthAgo, now));
            context.put("productionDataDetail", getProductionDataDetail(allProductionData));
        }
        
        if (lowerQuestion.contains("销售") || lowerQuestion.contains("收入") || lowerQuestion.contains("利润") || 
            lowerQuestion.contains("sales") || lowerQuestion.contains("revenue") || lowerQuestion.contains("profit")) {
            context.put("salesData", getSalesSummary(oneMonthAgo, now));
            context.put("salesDataDetail", getSalesDataDetail(allSalesData));
        }
        
        if (lowerQuestion.contains("库存") || lowerQuestion.contains("缺货") || lowerQuestion.contains("周转") || 
            lowerQuestion.contains("inventory") || lowerQuestion.contains("stockout") || lowerQuestion.contains("turnover")) {
            context.put("inventoryData", getInventorySummary());
            context.put("inventoryDataDetail", getInventoryDataDetail(allInventoryData));
        }
        
        if (lowerQuestion.contains("客户") || lowerQuestion.contains("反馈") || lowerQuestion.contains("满意") || 
            lowerQuestion.contains("customer") || lowerQuestion.contains("feedback") || lowerQuestion.contains("satisfaction")) {
            context.put("feedbackData", getFeedbackSummary(oneMonthAgo, now));
            context.put("feedbackDataDetail", getFeedbackDataDetail(allFeedbackData));
        }
        
        // If no specific keywords found, include all available data
        if (context.isEmpty()) {
            if (!allProductionData.isEmpty()) context.put("productionData", getProductionSummary(oneMonthAgo, now));
            if (!allSalesData.isEmpty()) context.put("salesData", getSalesSummary(oneMonthAgo, now));
            if (!allInventoryData.isEmpty()) context.put("inventoryData", getInventorySummary());
            if (!allFeedbackData.isEmpty()) context.put("feedbackData", getFeedbackSummary(oneMonthAgo, now));
        }

        return context;
    }

    /**
     * Build analysis prompt
     */
    private String buildAnalysisPrompt(String question, Map<String, Object> dataContext) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(SYSTEM_PROMPT).append("\n\n");
        prompt.append("用户问题: ").append(question).append("\n\n");
        prompt.append("相关数据:\n");
        
        dataContext.forEach((key, value) -> {
            prompt.append(key).append(": ").append(value).append("\n");
        });
        
        prompt.append("\n请基于以上数据提供详细的分析报告，包括：\n");
        prompt.append("1. 数据概览和关键指标分析\n");
        prompt.append("2. 趋势分析和异常识别\n");
        prompt.append("3. 问题诊断和原因分析\n");
        prompt.append("4. 改进建议和行动计划\n");
        prompt.append("5. 风险预警和机会识别\n");
        prompt.append("6. 具体的数据支撑和计算过程\n");
        
        return prompt.toString();
    }

    /**
     * Get production data summary
     */
    private Map<String, Object> getProductionSummary(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        List<ProductionData> productionData = excelDataService.getProductionDataByDateRange(startDate, endDate);
        
        if (!productionData.isEmpty()) {
            summary.put("totalProduction", productionData.stream().mapToInt(ProductionData::getProductionQuantity).sum());
            summary.put("totalDefects", productionData.stream().mapToInt(ProductionData::getDefectQuantity).sum());
            summary.put("averageEfficiency", productionData.stream().mapToDouble(ProductionData::getEfficiencyRate).average().orElse(0.0));
            summary.put("defectRate", calculateDefectRate(productionData));
            summary.put("productionLines", productionData.stream().map(ProductionData::getProductionLine).distinct().collect(Collectors.toList()));
        }
        
        return summary;
    }

    /**
     * Get detailed production data
     */
    private Map<String, Object> getProductionDataDetail(List<ProductionData> productionData) {
        Map<String, Object> detail = new HashMap<>();
        
        if (!productionData.isEmpty()) {
            // 按产品分组统计
            Map<String, List<ProductionData>> productGroups = productionData.stream()
                    .collect(Collectors.groupingBy(ProductionData::getProductName));
            
            Map<String, Object> productStats = new HashMap<>();
            productGroups.forEach((product, data) -> {
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalProduction", data.stream().mapToInt(ProductionData::getProductionQuantity).sum());
                stats.put("totalDefects", data.stream().mapToInt(ProductionData::getDefectQuantity).sum());
                stats.put("averageEfficiency", data.stream().mapToDouble(ProductionData::getEfficiencyRate).average().orElse(0.0));
                stats.put("defectRate", calculateDefectRate(data));
                productStats.put(product, stats);
            });
            
            detail.put("productStatistics", productStats);
            detail.put("totalRecords", productionData.size());
            detail.put("dateRange", getDateRange(productionData.stream().map(ProductionData::getProductionDate).collect(Collectors.toList())));
        }
        
        return detail;
    }

    /**
     * Get sales data summary
     */
    private Map<String, Object> getSalesSummary(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        List<SalesData> salesData = excelDataService.getSalesDataByDateRange(startDate, endDate);
        
        if (!salesData.isEmpty()) {
            summary.put("totalSales", salesData.stream().mapToDouble(SalesData::getSalesAmount).sum());
            summary.put("totalQuantity", salesData.stream().mapToInt(SalesData::getSalesQuantity).sum());
            summary.put("averageProfitMargin", salesData.stream().mapToDouble(SalesData::getProfitMargin).average().orElse(0.0));
            summary.put("regions", salesData.stream().map(SalesData::getRegion).distinct().collect(Collectors.toList()));
            summary.put("salesChannels", salesData.stream().map(SalesData::getSalesChannel).distinct().collect(Collectors.toList()));
        }
        
        return summary;
    }

    /**
     * Get detailed sales data
     */
    private Map<String, Object> getSalesDataDetail(List<SalesData> salesData) {
        Map<String, Object> detail = new HashMap<>();
        
        if (!salesData.isEmpty()) {
            // 按产品分组统计
            Map<String, List<SalesData>> productGroups = salesData.stream()
                    .collect(Collectors.groupingBy(SalesData::getProductName));
            
            Map<String, Object> productStats = new HashMap<>();
            productGroups.forEach((product, data) -> {
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalSales", data.stream().mapToDouble(SalesData::getSalesAmount).sum());
                stats.put("totalQuantity", data.stream().mapToInt(SalesData::getSalesQuantity).sum());
                stats.put("averageProfitMargin", data.stream().mapToDouble(SalesData::getProfitMargin).average().orElse(0.0));
                stats.put("regions", data.stream().map(SalesData::getRegion).distinct().collect(Collectors.toList()));
                productStats.put(product, stats);
            });
            
            detail.put("productStatistics", productStats);
            detail.put("totalRecords", salesData.size());
            detail.put("dateRange", getDateRange(salesData.stream().map(SalesData::getSalesDate).collect(Collectors.toList())));
        }
        
        return detail;
    }

    /**
     * Get inventory data summary
     */
    private Map<String, Object> getInventorySummary() {
        Map<String, Object> summary = new HashMap<>();
        
        List<InventoryData> inventoryData = excelDataService.getAllInventoryData();
        
        if (!inventoryData.isEmpty()) {
            summary.put("totalStock", inventoryData.stream().mapToInt(InventoryData::getCurrentStock).sum());
            summary.put("lowStockItems", inventoryData.stream().filter(i -> i.getCurrentStock() < i.getMinStockLevel()).count());
            summary.put("overStockItems", inventoryData.stream().filter(i -> i.getCurrentStock() > i.getMaxStockLevel()).count());
            summary.put("warehouses", inventoryData.stream().map(InventoryData::getWarehouseLocation).distinct().collect(Collectors.toList()));
        }
        
        return summary;
    }

    /**
     * Get detailed inventory data
     */
    private Map<String, Object> getInventoryDataDetail(List<InventoryData> inventoryData) {
        Map<String, Object> detail = new HashMap<>();
        
        if (!inventoryData.isEmpty()) {
            // 按产品分组统计
            Map<String, List<InventoryData>> productGroups = inventoryData.stream()
                    .collect(Collectors.groupingBy(InventoryData::getProductName));
            
            Map<String, Object> productStats = new HashMap<>();
            productGroups.forEach((product, data) -> {
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalStock", data.stream().mapToInt(InventoryData::getCurrentStock).sum());
                stats.put("averageStock", data.stream().mapToInt(InventoryData::getCurrentStock).average().orElse(0.0));
                stats.put("warehouses", data.stream().map(InventoryData::getWarehouseLocation).distinct().collect(Collectors.toList()));
                productStats.put(product, stats);
            });
            
            detail.put("productStatistics", productStats);
            detail.put("totalRecords", inventoryData.size());
        }
        
        return detail;
    }

    /**
     * Get customer feedback summary
     */
    private Map<String, Object> getFeedbackSummary(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        List<CustomerFeedback> feedbackData = excelDataService.getFeedbackDataByDateRange(startDate, endDate);
        
        if (!feedbackData.isEmpty()) {
            summary.put("totalFeedback", feedbackData.size());
            summary.put("averageSatisfaction", feedbackData.stream().mapToInt(CustomerFeedback::getSatisfactionScore).average().orElse(0.0));
            summary.put("complaints", feedbackData.stream().filter(f -> "投诉".equals(f.getFeedbackType())).count());
            summary.put("suggestions", feedbackData.stream().filter(f -> "建议".equals(f.getFeedbackType())).count());
            summary.put("pendingIssues", feedbackData.stream().filter(f -> "待处理".equals(f.getStatus())).count());
        }
        
        return summary;
    }

    /**
     * Get detailed feedback data
     */
    private Map<String, Object> getFeedbackDataDetail(List<CustomerFeedback> feedbackData) {
        Map<String, Object> detail = new HashMap<>();
        
        if (!feedbackData.isEmpty()) {
            // 按产品分组统计
            Map<String, List<CustomerFeedback>> productGroups = feedbackData.stream()
                    .collect(Collectors.groupingBy(CustomerFeedback::getProductName));
            
            Map<String, Object> productStats = new HashMap<>();
            productGroups.forEach((product, data) -> {
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalFeedback", data.size());
                stats.put("averageSatisfaction", data.stream().mapToInt(CustomerFeedback::getSatisfactionScore).average().orElse(0.0));
                stats.put("complaints", data.stream().filter(f -> "投诉".equals(f.getFeedbackType())).count());
                stats.put("suggestions", data.stream().filter(f -> "建议".equals(f.getFeedbackType())).count());
                productStats.put(product, stats);
            });
            
            detail.put("productStatistics", productStats);
            detail.put("totalRecords", feedbackData.size());
            detail.put("dateRange", getDateRange(feedbackData.stream().map(CustomerFeedback::getFeedbackDate).collect(Collectors.toList())));
        }
        
        return detail;
    }

    /**
     * Calculate defect rate
     */
    private double calculateDefectRate(List<ProductionData> productionData) {
        int totalProduction = productionData.stream().mapToInt(ProductionData::getProductionQuantity).sum();
        int totalDefects = productionData.stream().mapToInt(ProductionData::getDefectQuantity).sum();
        
        return totalProduction > 0 ? (double) totalDefects / totalProduction * 100 : 0.0;
    }

    /**
     * Generate chart data based on question and data context
     */
    private Map<String, Object> generateChartData(String question, Map<String, Object> dataContext) {
        Map<String, Object> chartData = new HashMap<>();
        String lowerQuestion = question.toLowerCase();
        
        // Production charts
        if (lowerQuestion.contains("生产") || lowerQuestion.contains("效率") || lowerQuestion.contains("缺陷") || 
            lowerQuestion.contains("production") || lowerQuestion.contains("efficiency") || lowerQuestion.contains("defect")) {
            
            if (dataContext.containsKey("productionDataDetail")) {
                Map<String, Object> productionDetail = (Map<String, Object>) dataContext.get("productionDataDetail");
                if (productionDetail.containsKey("productStatistics")) {
                    Map<String, Object> productStats = (Map<String, Object>) productionDetail.get("productStatistics");
                    
                    // Production efficiency chart
                    chartData.put("productionEfficiency", generateProductionEfficiencyChart(productStats));
                    
                    // Defect rate chart
                    chartData.put("defectRate", generateDefectRateChart(productStats));
                    
                    // Production trend chart
                    chartData.put("productionTrend", generateProductionTrendChart());
                }
            }
        }
        
        // Sales charts
        if (lowerQuestion.contains("销售") || lowerQuestion.contains("收入") || lowerQuestion.contains("利润") || 
            lowerQuestion.contains("sales") || lowerQuestion.contains("revenue") || lowerQuestion.contains("profit")) {
            
            if (dataContext.containsKey("salesDataDetail")) {
                Map<String, Object> salesDetail = (Map<String, Object>) dataContext.get("salesDataDetail");
                if (salesDetail.containsKey("productStatistics")) {
                    Map<String, Object> productStats = (Map<String, Object>) salesDetail.get("productStatistics");
                    
                    // Sales amount chart
                    chartData.put("salesAmount", generateSalesAmountChart(productStats));
                    
                    // Profit margin chart
                    chartData.put("profitMargin", generateProfitMarginChart(productStats));
                    
                    // Regional sales chart
                    chartData.put("regionalSales", generateRegionalSalesChart());
                }
            }
        }
        
        // Inventory charts
        if (lowerQuestion.contains("库存") || lowerQuestion.contains("缺货") || lowerQuestion.contains("周转") || 
            lowerQuestion.contains("inventory") || lowerQuestion.contains("stockout") || lowerQuestion.contains("turnover")) {
            
            if (dataContext.containsKey("inventoryDataDetail")) {
                Map<String, Object> inventoryDetail = (Map<String, Object>) dataContext.get("inventoryDataDetail");
                if (inventoryDetail.containsKey("productStatistics")) {
                    Map<String, Object> productStats = (Map<String, Object>) inventoryDetail.get("productStatistics");
                    
                    // Inventory level chart
                    chartData.put("inventoryLevel", generateInventoryLevelChart(productStats));
                    
                    // Warehouse distribution chart
                    chartData.put("warehouseDistribution", generateWarehouseDistributionChart());
                }
            }
        }
        
        // Customer feedback charts
        if (lowerQuestion.contains("客户") || lowerQuestion.contains("反馈") || lowerQuestion.contains("满意") || 
            lowerQuestion.contains("customer") || lowerQuestion.contains("feedback") || lowerQuestion.contains("satisfaction")) {
            
            if (dataContext.containsKey("feedbackDataDetail")) {
                Map<String, Object> feedbackDetail = (Map<String, Object>) dataContext.get("feedbackDataDetail");
                if (feedbackDetail.containsKey("productStatistics")) {
                    Map<String, Object> productStats = (Map<String, Object>) feedbackDetail.get("productStatistics");
                    
                    // Satisfaction score chart
                    chartData.put("satisfactionScore", generateSatisfactionScoreChart(productStats));
                    
                    // Feedback type distribution chart
                    chartData.put("feedbackTypeDistribution", generateFeedbackTypeDistributionChart());
                }
            }
        }
        
        return chartData;
    }

    /**
     * Get real-time dashboard data
     */
    public Map<String, Object> getRealTimeDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);
        
        // 获取所有数据
        List<ProductionData> allProductionData = excelDataService.getAllProductionData();
        List<SalesData> allSalesData = excelDataService.getAllSalesData();
        List<InventoryData> allInventoryData = excelDataService.getAllInventoryData();
        List<CustomerFeedback> allFeedbackData = excelDataService.getAllFeedbackData();
        
        // 获取基础统计
        Map<String, Object> productionSummary = getProductionSummary(oneMonthAgo, now);
        Map<String, Object> salesSummary = getSalesSummary(oneMonthAgo, now);
        Map<String, Object> inventorySummary = getInventorySummary();
        Map<String, Object> feedbackSummary = getFeedbackSummary(oneMonthAgo, now);
        
        // 添加详细的产品统计数据
        productionSummary.put("productStatistics", getProductionDataDetail(allProductionData).get("productStatistics"));
        salesSummary.put("productStatistics", getSalesDataDetail(allSalesData).get("productStatistics"));
        inventorySummary.put("productStatistics", getInventoryDataDetail(allInventoryData).get("productStatistics"));
        feedbackSummary.put("productStatistics", getFeedbackDataDetail(allFeedbackData).get("productStatistics"));
        
        dashboard.put("production", productionSummary);
        dashboard.put("sales", salesSummary);
        dashboard.put("inventory", inventorySummary);
        dashboard.put("feedback", feedbackSummary);
        dashboard.put("lastUpdated", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return dashboard;
    }

    /**
     * Get date range from list of dates
     */
    private String getDateRange(List<LocalDateTime> dates) {
        if (dates.isEmpty()) return "无数据";
        
        LocalDateTime minDate = dates.stream().min(LocalDateTime::compareTo).orElse(null);
        LocalDateTime maxDate = dates.stream().max(LocalDateTime::compareTo).orElse(null);
        
        if (minDate != null && maxDate != null) {
            return minDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 至 " + 
                   maxDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        
        return "无数据";
    }

    // Chart generation methods
    private Map<String, Object> generateProductionEfficiencyChart(Map<String, Object> productStats) {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        
        productStats.forEach((product, stats) -> {
            Map<String, Object> stat = (Map<String, Object>) stats;
            categories.add(product);
            data.add((Double) stat.get("averageEfficiency"));
        });
        
        chartData.put("type", "bar");
        chartData.put("title", "各产品生产效率对比");
        chartData.put("categories", categories);
        chartData.put("data", data);
        chartData.put("yAxisTitle", "效率率 (%)");
        
        return chartData;
    }

    private Map<String, Object> generateDefectRateChart(Map<String, Object> productStats) {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        
        productStats.forEach((product, stats) -> {
            Map<String, Object> stat = (Map<String, Object>) stats;
            categories.add(product);
            data.add((Double) stat.get("defectRate"));
        });
        
        chartData.put("type", "line");
        chartData.put("title", "各产品缺陷率趋势");
        chartData.put("categories", categories);
        chartData.put("data", data);
        chartData.put("yAxisTitle", "缺陷率 (%)");
        
        return chartData;
    }

    private Map<String, Object> generateProductionTrendChart() {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = Arrays.asList("1月", "2月", "3月", "4月");
        List<Integer> data = Arrays.asList(850, 920, 880, 950);
        
        chartData.put("type", "line");
        chartData.put("title", "月度生产趋势");
        chartData.put("categories", categories);
        chartData.put("data", data);
        chartData.put("yAxisTitle", "生产数量");
        
        return chartData;
    }

    private Map<String, Object> generateSalesAmountChart(Map<String, Object> productStats) {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        
        productStats.forEach((product, stats) -> {
            Map<String, Object> stat = (Map<String, Object>) stats;
            categories.add(product);
            data.add((Double) stat.get("totalSales"));
        });
        
        chartData.put("type", "bar");
        chartData.put("title", "各产品销售额对比");
        chartData.put("categories", categories);
        chartData.put("data", data);
        chartData.put("yAxisTitle", "销售额 (万元)");
        
        return chartData;
    }

    private Map<String, Object> generateProfitMarginChart(Map<String, Object> productStats) {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        
        productStats.forEach((product, stats) -> {
            Map<String, Object> stat = (Map<String, Object>) stats;
            categories.add(product);
            data.add((Double) stat.get("averageProfitMargin"));
        });
        
        chartData.put("type", "pie");
        chartData.put("title", "各产品利润率分布");
        chartData.put("categories", categories);
        chartData.put("data", data);
        
        return chartData;
    }

    private Map<String, Object> generateRegionalSalesChart() {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = Arrays.asList("北京", "上海", "广州", "深圳", "杭州");
        List<Double> data = Arrays.asList(1200.0, 980.0, 850.0, 1100.0, 750.0);
        
        chartData.put("type", "bar");
        chartData.put("title", "各地区销售表现");
        chartData.put("categories", categories);
        chartData.put("data", data);
        chartData.put("yAxisTitle", "销售额 (万元)");
        
        return chartData;
    }

    private Map<String, Object> generateInventoryLevelChart(Map<String, Object> productStats) {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        
        productStats.forEach((product, stats) -> {
            Map<String, Object> stat = (Map<String, Object>) stats;
            categories.add(product);
            data.add((Integer) stat.get("totalStock"));
        });
        
        chartData.put("type", "bar");
        chartData.put("title", "各产品库存水平");
        chartData.put("categories", categories);
        chartData.put("data", data);
        chartData.put("yAxisTitle", "库存数量");
        
        return chartData;
    }

    private Map<String, Object> generateWarehouseDistributionChart() {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = Arrays.asList("北京仓库", "上海仓库", "广州仓库", "深圳仓库", "杭州仓库");
        List<Integer> data = Arrays.asList(150, 120, 200, 180, 90);
        
        chartData.put("type", "pie");
        chartData.put("title", "各仓库库存分布");
        chartData.put("categories", categories);
        chartData.put("data", data);
        
        return chartData;
    }

    private Map<String, Object> generateSatisfactionScoreChart(Map<String, Object> productStats) {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        
        productStats.forEach((product, stats) -> {
            Map<String, Object> stat = (Map<String, Object>) stats;
            categories.add(product);
            data.add((Double) stat.get("averageSatisfaction"));
        });
        
        chartData.put("type", "bar");
        chartData.put("title", "各产品客户满意度");
        chartData.put("categories", categories);
        chartData.put("data", data);
        chartData.put("yAxisTitle", "满意度评分");
        
        return chartData;
    }

    private Map<String, Object> generateFeedbackTypeDistributionChart() {
        Map<String, Object> chartData = new HashMap<>();
        List<String> categories = Arrays.asList("投诉", "建议", "表扬", "咨询");
        List<Integer> data = Arrays.asList(15, 25, 35, 10);
        
        chartData.put("type", "pie");
        chartData.put("title", "客户反馈类型分布");
        chartData.put("categories", categories);
        chartData.put("data", data);
        
        return chartData;
    }
} 