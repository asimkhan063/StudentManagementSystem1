package com.asim.studentmanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.asim.studentmanagement.service.DashboardService;
import com.asim.studentmanagement.service.EnrollmentService;

@Controller
public class DashboardController {

    @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(DashboardController.class);  // ✅ Fixed

    private final EnrollmentService enrollmentService;
    private final DashboardService dashboardService;

    public DashboardController(EnrollmentService enrollmentService, DashboardService dashboardService) {
        this.enrollmentService = enrollmentService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("dashboardStats", dashboardService.getDashboardStats());
        model.addAttribute("students", enrollmentService.getRecentlyEnrolledStudents());
        return "dashboard";
    }
}