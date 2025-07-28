package com.demo.ai.model;

import java.time.LocalDateTime;

/**
 * 库存数据模型
 */
public class InventoryData {
    
    private Long id;
    private String productName;
    private Integer currentStock;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private String warehouseLocation;
    private Double unitCost;
    private LocalDateTime lastUpdated;
    private String supplierId;
    private LocalDateTime createdTime;
    
    public InventoryData() {
        this.createdTime = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
    
    public InventoryData(String productName, Integer currentStock, Integer minStockLevel, Integer maxStockLevel,
                        String warehouseLocation, Double unitCost, String supplierId) {
        this.productName = productName;
        this.currentStock = currentStock;
        this.minStockLevel = minStockLevel;
        this.maxStockLevel = maxStockLevel;
        this.warehouseLocation = warehouseLocation;
        this.unitCost = unitCost;
        this.supplierId = supplierId;
        this.createdTime = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
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

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public Integer getMaxStockLevel() {
        return maxStockLevel;
    }

    public void setMaxStockLevel(Integer maxStockLevel) {
        this.maxStockLevel = maxStockLevel;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
} 