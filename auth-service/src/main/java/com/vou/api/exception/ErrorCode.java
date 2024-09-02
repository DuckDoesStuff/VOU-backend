package com.vou.api.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USERNAME_EXISTED(4001, "Username already exist"),
    USER_NOT_EXIST(4014, "This username doesn't exist"),
    UNAUTHENTICATED(4010, "Unauthenticated"),
    INVALID_TOKEN(4011, "Invalid token"),
    INVALID_OTP(4012, "Invalid OTP"),
    WRONG_CREDENTIAL(4013, "Wrong password")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
