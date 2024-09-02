package com.vou.api.controller;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ReportTotalVoucherByBrandID;
import com.vou.api.dto.VoucherReportDTO;
import com.vou.api.entity.VoucherType;
import com.vou.api.service.VoucherService;
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

    @GetMapping("/voucher/{eventID}")
    public ApiResponse<List<VoucherReportDTO>> getVouchersReportInAEvent(@PathVariable Long eventID) {
        return voucherService.getVouchersDistributedInAEvent(eventID);
    }
    @GetMapping("/voucher")
    public List<VoucherType> getAllVoucherType() {
        return voucherService.getAllVoucherType();
    }

    // Total all voucher value of a brand ( sum_of(value * voucherQuantity) )
    @GetMapping("/voucher/total-by-brand-event")
    public ApiResponse<List<ReportTotalVoucherByBrandID>> getTotalValueGroupedByBrandAndEvent() {
        return voucherService.findTotalValueGroupedByBrandAndEvent();
    }
}

