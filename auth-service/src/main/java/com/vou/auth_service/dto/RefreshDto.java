package com.vou.auth_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshDto {
    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Refresh token is required")
    private String refreshToken;
}
