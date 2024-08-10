package com.example.demo.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_EXISTED(4001, "This email already exist"),
    PHONE_EXISTED(4002, "This phone number already exist"),
    USERNAME_EXISTED(4003, "This username already used"),
    BRANDNAME_EXISTED(4003, "This brandname already used"),
    USER_NOT_EXIST(4040, "This user doesn't exist"),
    UNAUTHENTICATED(4010, "Unauthenticated"),
    INTERNAL(500, "Internal error"),
    PROFILE_NOT_FOUND(4041, "This profile doesn't exist");
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
