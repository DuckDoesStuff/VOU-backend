package com.example.ReportService.controller;

import com.example.ReportService.dto.ApiResponse;
import com.example.ReportService.dto.VoucherReportDTO;
import com.example.ReportService.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class VoucherController {
    private final VoucherService voucherService;
    @GetMapping("/voucher/:eventID")
    public ApiResponse<List<VoucherReportDTO>> getVouchersReportInAEvent(@PathVariable Long eventID) {
        return voucherService.getVouchersDistributedInAEvent(eventID);
    }

}

