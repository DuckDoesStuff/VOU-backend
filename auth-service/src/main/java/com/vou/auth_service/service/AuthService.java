package com.vou.auth_service.service;

import com.vou.auth_service.dto.request.LoginDto;
import org.springframework.stereotype.Component;

@Component
public class AuthService {
    public String login(LoginDto loginDto) {
        return "User " + loginDto.getEmail() + " logged in";
    }

    public void logout() {

    }

    public void register() {

    }

    public void logoutAll() {

    }

    public void refresh() {

    }
}
