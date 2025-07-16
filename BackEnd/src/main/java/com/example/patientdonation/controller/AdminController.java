package com.example.patientdonation.controller;

import com.example.patientdonation.dto.AdminSummaryDTO;
import com.example.patientdonation.service.impl.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/summary")
    public ResponseEntity<AdminSummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(adminService.getSummary());
    }
}
