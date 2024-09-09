package com.vou.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class VoucherException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;
    private HttpStatus httpStatus;

    public VoucherException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
