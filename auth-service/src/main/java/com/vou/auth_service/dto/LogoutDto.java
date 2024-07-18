package com.vou.auth_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LogoutDto {
    @NotNull(message = "Refresh token is required")
    private String refreshToken;
}
