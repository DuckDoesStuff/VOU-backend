package com.vou.auth_service.controller;

import com.vou.auth_service.dto.*;
import com.vou.auth_service.entity.Auth;
import com.vou.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ApiResponse<List<Auth>> getAuthList() {
        ApiResponse<List<Auth>> authList = new ApiResponse<>();
        authList.setResult(authService.getAuthList());
        return authList;
    }

    @PostMapping("/token")
    public ApiResponse<AuthResponse> getToken(@Valid @RequestBody AuthDto authDto) {
        ApiResponse<AuthResponse> response = new ApiResponse<>();
        AuthResponse authResponse = authService.authenticate(authDto);
        response.setCode(200);
        response.setMessage("Authorized");
        response.setResult(authResponse);
        return response;
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@Valid @RequestBody RefreshDto refreshDto) {
        ApiResponse<AuthResponse> response = new ApiResponse<>();
        AuthResponse authResponse = authService.refresh(refreshDto);
        response.setCode(200);
        response.setMessage("Authorized");
        response.setResult(authResponse);
        return response;
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyToken(@Valid @RequestBody VerifyDto verifyDto) {
        boolean verified = authService.verify(verifyDto.getToken());
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(verified ? 200:401);
        response.setMessage(verified ? "Authorized" : "Unauthorized");
        return response;
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody LogoutDto logoutDto) {
        authService.logout(logoutDto);
    }

    @PostMapping("/register")
    public ApiResponse<Auth> register(@Valid @RequestBody AuthDto authDto) {
        ApiResponse<Auth> apiResponse = new ApiResponse<>();
        apiResponse.setResult(authService.createAuth(authDto));
        apiResponse.setCode(200);
        return apiResponse;
    }

    // FOR DEBUGGING ONLY
    @DeleteMapping("/delete/{username}")
    public ApiResponse<String> delete(@PathVariable String username) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        authService.deleteAuth(username);
        apiResponse.setCode(200);
        apiResponse.setMessage("User deleted successfully");
        return apiResponse;
    }

    @GetMapping("/protected")
    public ApiResponse<String> protectedRoute() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Hey there");
        response.setCode(200);
        return response;
    }

    @PostMapping("/test")
    public ApiResponse<String> testRoute() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Testing 123");
        response.setCode(200);
        return response;
    }
}
