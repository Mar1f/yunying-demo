package com.demo.ai.model;

import java.time.LocalDateTime;

/**
 * 客户反馈数据模型
 */
public class CustomerFeedback {
    
    private Long id;
    private String customerId;
    private String productName;
    private String feedbackType; // 投诉、建议、表扬
    private String feedbackContent;
    private Integer satisfactionScore; // 1-5分
    private LocalDateTime feedbackDate;
    private String status; // 待处理、处理中、已解决
    private String priority; // 高、中、低
    private LocalDateTime createdTime;
    
    public CustomerFeedback() {
        this.createdTime = LocalDateTime.now();
    }
    
    public CustomerFeedback(String customerId, String productName, String feedbackType, String feedbackContent,
                           Integer satisfactionScore, LocalDateTime feedbackDate, String status, String priority) {
        this.customerId = customerId;
        this.productName = productName;
        this.feedbackType = feedbackType;
        this.feedbackContent = feedbackContent;
        this.satisfactionScore = satisfactionScore;
        this.feedbackDate = feedbackDate;
        this.status = status;
        this.priority = priority;
        this.createdTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public Integer getSatisfactionScore() {
        return satisfactionScore;
    }

    public void setSatisfactionScore(Integer satisfactionScore) {
        this.satisfactionScore = satisfactionScore;
    }

    public LocalDateTime getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
} 