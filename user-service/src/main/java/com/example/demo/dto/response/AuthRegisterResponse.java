package com.example.demo.dto.response;

import com.example.demo.enumerate.ProfileState;
import com.example.demo.enumerate.Role;
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
