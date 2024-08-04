package com.vou.auth_service.dto.response;

import com.vou.auth_service.enumerate.ProfileState;
import com.vou.auth_service.enumerate.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterResponse {
    private String username;
    private String phone;
    private Role role;
    private ProfileState state;
}
