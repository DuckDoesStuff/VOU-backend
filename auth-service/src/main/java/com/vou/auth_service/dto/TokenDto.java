package com.vou.auth_service.dto;

import com.vou.auth_service.enumerate.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDto {
    private String profileID;
    private Role role;
    private String token;
    private String refreshToken;
}
