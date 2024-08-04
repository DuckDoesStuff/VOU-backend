package com.vou.auth_service.exception;


import com.vou.auth_service.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setMessage(e.getFieldError().getDefaultMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleAuthException(AuthException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        if(errorCode.getCode() != 0)
            apiResponse.setCode(errorCode.getCode());

        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getCode()).body(apiResponse);
    }
}
