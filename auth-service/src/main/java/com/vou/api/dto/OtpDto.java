package com.vou.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpDto {
    @NotNull(message = "Otp code is requried")
    String otp;
}
