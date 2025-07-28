package com.demo.ai.model;

import java.time.LocalDateTime;

/**
 * 销售数据模型
 */
public class SalesData {
    
    private Long id;
    private String productName;
    private Integer salesQuantity;
    private Double salesAmount;
    private LocalDateTime salesDate;
    private String customerId;
    private String region;
    private String salesChannel;
    private Double profitMargin;
    private LocalDateTime createdTime;
    
    public SalesData() {
        this.createdTime = LocalDateTime.now();
    }
    
    public SalesData(String productName, Integer salesQuantity, Double salesAmount,
                    LocalDateTime salesDate, String customerId, String region, String salesChannel, Double profitMargin) {
        this.productName = productName;
        this.salesQuantity = salesQuantity;
        this.salesAmount = salesAmount;
        this.salesDate = salesDate;
        this.customerId = customerId;
        this.region = region;
        this.salesChannel = salesChannel;
        this.profitMargin = profitMargin;
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

    public Integer getSalesQuantity() {
        return salesQuantity;
    }

    public void setSalesQuantity(Integer salesQuantity) {
        this.salesQuantity = salesQuantity;
    }

    public Double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public LocalDateTime getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDateTime salesDate) {
        this.salesDate = salesDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public Double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(Double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
} 