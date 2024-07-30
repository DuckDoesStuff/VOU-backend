package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDto {
    @NotNull(message = "Username is required")
    private String username;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Phone number is required")
    private String phone;
    @NotNull(message = "Email is required")
    private String email;
}