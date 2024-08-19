package com.vou.api.dto.user;

import com.vou.api.enumerate.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegisterDto {
    @NotNull(message = "Username is required")
    private String username;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Phone number is required")
    private String phone;
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Role is required")
    private Role role;
}
