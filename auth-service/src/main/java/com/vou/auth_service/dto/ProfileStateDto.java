package com.vou.auth_service.dto;

import com.vou.auth_service.enumerate.ProfileState;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileStateDto {
    private ProfileState state;
}
