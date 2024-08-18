package com.vou.api.controller;

import com.vou.api.dto.OtpDto;
import com.vou.api.dto.response.ApiResponse;
import com.vou.api.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/otp")
public class OtpController {
    public OtpService otpService;

    @Autowired
    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/{phone}")
    public ApiResponse<Boolean> verifyPhone(@PathVariable String phone, @RequestBody OtpDto otpDto) {
        return ApiResponse.<Boolean>builder()
                .result(otpService.verify(phone, otpDto.getOtp()))
                .code(200)
                .message("Otp verified successfully")
                .build();
    }
}
