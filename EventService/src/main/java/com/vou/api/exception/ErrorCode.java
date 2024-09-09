package com.vou.api.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VOUCHER_OUT_OF_STOCK(4001, "This voucher is no longer available"),
    VOUCHER_NOT_FOUND(4040, "This voucher doesn't exist"),
    VOUCHER_EXPIRED(4010, "This voucher has been expired"),
    VOUCHER_ALREADY_SAVED(2001, "You have already got this voucher"),
    VOUCHER_USED(2002, "This voucher has been used")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
