package com.vou.auth_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthException extends RuntimeException{
    private ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
