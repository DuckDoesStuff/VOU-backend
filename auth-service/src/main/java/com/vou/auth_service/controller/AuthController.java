package com.vou.auth_service.controller;

import com.vou.auth_service.dto.request.ApiResponse;
import com.vou.auth_service.dto.request.LoginDto;
import com.vou.auth_service.exception.TestCustomException;
import com.vou.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ApiResponse<String> hello() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Success");
        response.setResult("Hello world");
        response.setCode(200);
        return response;
    }

    @PostMapping("exception")
    public void testException() {
        throw new TestCustomException("This is a test message", 69);
    }

    @PostMapping("login")
    public ApiResponse<String> login(@Valid @RequestBody LoginDto loginDto) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setResult(authService.login(loginDto));
        response.setCode(200);
        return response;
    }

}
