package com.vou.auth_service.controller;

import com.vou.auth_service.dto.*;
import com.vou.auth_service.dto.response.ApiResponse;
import com.vou.auth_service.dto.response.AuthRegisterResponse;
import com.vou.auth_service.dto.response.AuthResponse;
import com.vou.auth_service.entity.Auth;
import com.vou.auth_service.exception.AuthException;
import com.vou.auth_service.exception.ErrorCode;
import com.vou.auth_service.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public AuthService authService;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected int validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected int refreshableDuration;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ApiResponse<List<Auth>> getAuthList() {
        return ApiResponse.<List<Auth>>builder()
                .code(200)
                .result(authService.getAuthList())
                .build();
    }

    @PostMapping("/token")
    public ApiResponse<AuthResponse> getToken(@Valid @RequestBody AuthDto authDto, HttpServletResponse response) {
        TokenDto result = authService.authenticate(authDto);

        Cookie at = new Cookie("at", result.getToken());
        at.setHttpOnly(true);
        at.setMaxAge(validDuration); // expires after 1 hour
        at.setPath("/");

        Cookie rt = new Cookie("rt", result.getRefreshToken());
        rt.setHttpOnly(true);
        rt.setMaxAge(refreshableDuration); // expires in 10 hours
        rt.setPath("/");

        response.addCookie(at);
        response.addCookie(rt);

        AuthResponse authResponse = new AuthResponse(result.getProfileID(), result.getRole().toString(), true);

        return ApiResponse.<AuthResponse>builder()
                .result(authResponse)
                .code(200)
                .message("Authenticated")
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@Valid @RequestBody RefreshDto refreshDto, @CookieValue("rt") String refreshToken, HttpServletResponse response) {
        TokenDto result = authService.refresh(refreshDto, refreshToken);

        Cookie at = new Cookie("at", result.getToken());
        at.setHttpOnly(true);
        at.setMaxAge(validDuration); // expires after 1 hour
        at.setPath("/");

        Cookie rt = new Cookie("rt", result.getRefreshToken());
        rt.setHttpOnly(true);
        rt.setMaxAge(refreshableDuration); // expires in 10 hours
        rt.setPath("/");

        response.addCookie(at);
        response.addCookie(rt);

        AuthResponse authResponse = new AuthResponse(result.getProfileID(), result.getRole().toString(), true);

        return ApiResponse.<AuthResponse>builder()
                .result(authResponse)
                .code(200)
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyToken(@Valid @RequestBody VerifyDto verifyDto) {
        boolean verified = authService.verify(verifyDto.getToken());
        if (!verified)
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Authorized")
                .build();
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody LogoutDto logoutDto) {
        authService.logout(logoutDto);
    }

    @PostMapping("/register")
    public ApiResponse<AuthRegisterResponse> register(@Valid @RequestBody RegisterDto registerDto) {
        return ApiResponse.<AuthRegisterResponse>builder()
                .result(authService.createAuth(registerDto))
                .message("Successfully created new authentication credential")
                .code(200)
                .build();
    }

    // FOR DEBUGGING ONLY
    @DeleteMapping("/delete/{username}")
    public ApiResponse<String> delete(@PathVariable String username) {
        return ApiResponse.<String>builder()
                .code(200)
                .message("Authentication deleted successfully")
                .build();
    }

    @GetMapping("/protected")
    public ApiResponse<String> protectedRoute() {
        return ApiResponse.<String>builder()
                .message("Test protected route")
                .code(200)
                .build();
    }

    @PostMapping("/test")
    public ApiResponse<String> testRoute() {
        return ApiResponse.<String>builder()
                .message("Test route")
                .code(200)
                .build();
    }
}
