package com.example.demo.dto.request;

import com.example.demo.enumerate.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;


@Data
@AllArgsConstructor
public class AuthRegisterRequest {
    @NotNull(message = "Id is required")
    private UUID id;
    @NotNull(message = "Username is required")
    private String username;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Phone number is required")
    private String phone;
    @NotNull(message = "Role is required")
    private Role role;
}
