package com.vou.auth_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_EXISTED(1001, "Username already exist"),
    USER_NOT_EXIST(404, "This user doesn't exist"),
    UNAUTHENTICATED(401, "Unauthenticated")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
