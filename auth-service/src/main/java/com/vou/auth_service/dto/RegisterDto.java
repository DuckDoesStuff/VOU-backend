package com.vou.auth_service.dto;

import com.vou.auth_service.enumerate.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {
    @NotNull(message = "Id is required")
    private String id;
    @NotNull(message = "Username is required")
    private String username;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Phone number is required")
    private String phone;
    @NotNull(message = "A role is required")
    @Enumerated(EnumType.STRING)
    private Role role;
}
