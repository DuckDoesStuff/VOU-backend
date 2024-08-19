package com.eventservice.EventService.controller;

import com.eventservice.EventService.dto.ApiResponse;
import com.eventservice.EventService.dto.VoucherDto;
import com.eventservice.EventService.entity.VoucherType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event/voucher")
public class VoucherController {
    @GetMapping
    public ResponseEntity<ApiResponse<String>> getVoucher() {
        return new ResponseEntity<ApiResponse<String>>(
                new ApiResponse<String>(
                        HttpStatus.OK.value(),
                        "Hey there",
                        "Result"),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherType>> createVoucher(@RequestBody @Valid VoucherDto voucherDto) {
        return null;
    }
}
