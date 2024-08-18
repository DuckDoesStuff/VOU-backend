package com.eventservice.EventService.controller;

import com.eventservice.EventService.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
