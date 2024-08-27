package com.vou.api.controller;

import com.vou.api.dto.*;
import com.vou.api.dto.response.ApiResponse;
import com.vou.api.dto.response.AuthRegisterResponse;
import com.vou.api.dto.response.AuthResponse;
import com.vou.api.entity.Auth;
import com.vou.api.enumerate.ProfileState;
import com.vou.api.enumerate.Role;
import com.vou.api.exception.AuthException;
import com.vou.api.exception.ErrorCode;
import com.vou.api.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
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
    public ApiResponse<AuthResponse> getToken(@RequestParam(name="mobile", defaultValue = "false") String mobile, @Valid @RequestBody AuthDto authDto, HttpServletResponse response) {
        TokenDto result = authService.authenticate(authDto);

        boolean isHttpOnly = !mobile.equalsIgnoreCase("true");
        Cookie at = new Cookie("at", result.getToken());
        at.setHttpOnly(isHttpOnly);
        at.setMaxAge(validDuration); // expires after 1 hour
        at.setPath("/");

        Cookie rt = new Cookie("rt", result.getRefreshToken());
        rt.setHttpOnly(isHttpOnly);
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
    public ApiResponse<AuthResponse> refreshToken(@RequestParam(name="mobile", defaultValue = "false") String mobile, @Valid @RequestBody RefreshDto refreshDto, @CookieValue("rt") String refreshToken, HttpServletResponse response) {
        TokenDto result = authService.refresh(refreshDto, refreshToken);

        boolean isHttpOnly = !mobile.equalsIgnoreCase("true");
        Cookie at = new Cookie("at", result.getToken());
        at.setHttpOnly(isHttpOnly);
        at.setMaxAge(validDuration); // expires after 1 hour
        at.setPath("/");

        Cookie rt = new Cookie("rt", result.getRefreshToken());
        rt.setHttpOnly(isHttpOnly);
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

    @KafkaListener(topics = "auth-create-topic")
    public void authCreateListener(AuthRegisterDto authRegisterDto) {
        authService.createAuth(authRegisterDto);
    }

    @KafkaListener(topics = "auth-update")
    public void authUpdateRole(String message) {
        String[] split = message.split("_");
        log.info(message);
        authService.updateAuth(split[0], Role.valueOf(split[1]), ProfileState.valueOf(split[2]));
    }

    @KafkaListener(topics = "auth-delete")
    public void authDelete(String message) {
        authService.deleteAuth(message);
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
