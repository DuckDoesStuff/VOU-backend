package com.vou.api.exception;


import com.vou.api.dto.ApiResponse;
import jakarta.security.auth.message.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(VoucherException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleVoucherException(VoucherException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if (errorCode != null) {
            apiResponse.setMessage(errorCode.getMessage());

            return ResponseEntity.status(errorCode.getCode() / 10).body(apiResponse);
        }
        else {
            apiResponse.setMessage(e.getMessage());

            return ResponseEntity.status(e.getHttpStatus()).body(apiResponse);
        }
    }
}
