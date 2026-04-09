package com.backend.librarycrm.controller;

import com.backend.librarycrm.dto.response.BaseResponse;
import com.backend.librarycrm.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getSummary() {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getSummaryStats(), "Lấy thống kê tổng quan thành công"));
    }
}