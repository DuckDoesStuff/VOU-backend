package com.vou.auth_service.dto;

import com.vou.auth_service.enumerate.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthDto {
    @NotNull(message = "Username is required")
    private String username;
    @NotNull(message = "Password is required")
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
