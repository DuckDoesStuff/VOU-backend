package com.vou.api.dto.response;

import com.vou.api.enumerate.ProfileState;
import com.vou.api.enumerate.Role;
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
