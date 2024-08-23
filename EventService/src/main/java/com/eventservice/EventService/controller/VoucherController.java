package com.eventservice.EventService.controller;

import com.eventservice.EventService.dto.ApiResponse;
import com.eventservice.EventService.dto.DecreaseVoucherDTO;
import com.eventservice.EventService.dto.VoucherDto;
import com.eventservice.EventService.entity.VoucherType;
import com.eventservice.EventService.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event/voucher")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VoucherDto>>> getAllVouchers() {
        return voucherService.getAllVouchers();
    }

    @GetMapping("/{eventID}")
    public ResponseEntity<ApiResponse<List<VoucherType>>> getAllVouchersOfAnEvent(@PathVariable Long eventID) {
        return voucherService.getVouchersByEvent(eventID);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherDto>> createVoucher(@RequestBody @Valid VoucherDto voucherDto) {
        return voucherService.createVoucherForEvent(voucherDto);
    }
    @PostMapping("decrease_quantity")
    public ResponseEntity<ApiResponse<String>> decreaseVoucherType(@RequestBody DecreaseVoucherDTO dto) {
        return voucherService.decreaseTotalQuantity(dto.getVoucherTypeID(), dto.getTotalDecreased());
    }
}
