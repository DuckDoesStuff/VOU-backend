package com.vou.auth_service.controller;

import com.vou.auth_service.dto.OtpDto;
import com.vou.auth_service.dto.response.ApiResponse;
import com.vou.auth_service.service.OtpService;
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
    public ApiResponse<String> verifyPhone(@PathVariable String phone, @RequestBody OtpDto otpDto) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        boolean verified = otpService.verify(phone, otpDto.getOtp());
        apiResponse.setCode(verified ? 200 : 400);
        apiResponse.setMessage(verified ? "Otp verified successfully" : "Invalid Otp");
        return apiResponse;
    }
}
