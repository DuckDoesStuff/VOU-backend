package com.vou.auth_service.dto;

import com.vou.auth_service.enumerate.Role;
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

    @NotNull(message = "Role is required")
    private Role role;
}
