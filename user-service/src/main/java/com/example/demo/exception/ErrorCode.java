package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_EXISTED(403, "This already exist"),
    EMAIL_EXISTED(400, "This email already exist"),
    PHONE_EXISTED(400, "This phone number already exist"),
    USERNAME_EXISTED(400, "This username already used"),
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
