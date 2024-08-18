package com.vou.api.exception;


import com.vou.api.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleAuthException(AuthException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Object> apiResponse = ApiResponse.builder().build();
        if (errorCode != null) {
            if(errorCode.getCode() != 0)
                apiResponse.setCode(errorCode.getCode());

            apiResponse.setMessage(errorCode.getMessage());

            return ResponseEntity.status(errorCode.getCode() / 10).body(apiResponse);
        }
        else {
            apiResponse.setCode(e.getHttpStatus().value());
            apiResponse.setMessage(e.getMessage());

            return ResponseEntity.status(e.getHttpStatus()).body(apiResponse);
        }
    }

    @ExceptionHandler(OtpException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleAuthException(OtpException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Object> apiResponse = ApiResponse.builder().build();
        if(errorCode.getCode() != 0)
            apiResponse.setCode(errorCode.getCode());

        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getCode() / 10).body(apiResponse);
    }
}
