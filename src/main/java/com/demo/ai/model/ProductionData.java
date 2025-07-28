package com.demo.ai.model;

import java.time.LocalDateTime;

/**
 * 生产数据模型
 */
public class ProductionData {
    
    private Long id;
    private String productName;
    private Integer productionQuantity;
    private Integer defectQuantity;
    private LocalDateTime productionDate;
    private String productionLine;
    private Double costPerUnit;
    private Double efficiencyRate;
    private LocalDateTime createdTime;
    
    public ProductionData() {
        this.createdTime = LocalDateTime.now();
    }
    
    public ProductionData(String productName, Integer productionQuantity, Integer defectQuantity, 
                         LocalDateTime productionDate, String productionLine, Double costPerUnit, Double efficiencyRate) {
        this.productName = productName;
        this.productionQuantity = productionQuantity;
        this.defectQuantity = defectQuantity;
        this.productionDate = productionDate;
        this.productionLine = productionLine;
        this.costPerUnit = costPerUnit;
        this.efficiencyRate = efficiencyRate;
        this.createdTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductionQuantity() {
        return productionQuantity;
    }

    public void setProductionQuantity(Integer productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public Integer getDefectQuantity() {
        return defectQuantity;
    }

    public void setDefectQuantity(Integer defectQuantity) {
        this.defectQuantity = defectQuantity;
    }

    public LocalDateTime getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDateTime productionDate) {
        this.productionDate = productionDate;
    }

    public String getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(String productionLine) {
        this.productionLine = productionLine;
    }

    public Double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(Double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public Double getEfficiencyRate() {
        return efficiencyRate;
    }

    public void setEfficiencyRate(Double efficiencyRate) {
        this.efficiencyRate = efficiencyRate;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
} 