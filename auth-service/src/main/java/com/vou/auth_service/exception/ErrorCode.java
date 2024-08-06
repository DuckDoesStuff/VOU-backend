package com.vou.auth_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USERNAME_EXISTED(4001, "Username already exist"),
    USER_NOT_EXIST(4040, "This username doesn't exist"),
    UNAUTHENTICATED(4010, "Unauthenticated"),
    INVALID_TOKEN(4011, "Invalid token"),
    INVALID_OTP(4012, "Invalid OTP")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
