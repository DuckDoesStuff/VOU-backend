package com.vou.api.dto;

import com.vou.api.enumerate.Role;
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
