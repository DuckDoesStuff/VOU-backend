package com.vou.api.dto;

import com.vou.api.enumerate.Role;
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
