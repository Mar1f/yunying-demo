package com.demo.ai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 看板页面视图控制器
 */
@Controller
public class DashboardViewController {

    /**
     * 主页
     */
    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

} 