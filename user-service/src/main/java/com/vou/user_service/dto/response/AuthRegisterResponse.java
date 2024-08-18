package com.vou.user_service.dto.response;

import com.vou.user_service.enumerate.ProfileState;
import com.vou.user_service.enumerate.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterResponse {
    private String name;
    private String phone;
    private Role role;
    private ProfileState state;
}
