package com.vou.api.dto.brand;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrandRegisterDto {
    @NotNull(message = "Login name is required")
    private String brandname;
    @NotNull(message = "Brand display name is required")
    private String displayName;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Phone number is required")
    private String phone;
    @NotNull(message = "Email is required")
    private String email;
}
