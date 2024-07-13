package com.vou.auth_service.exception;


import com.vou.auth_service.dto.request.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setMessage(e.getFieldError().getDefaultMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(TestCustomException.class)
    public ResponseEntity<ApiResponse>
    handleTestCustomException(TestCustomException e) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(e.getCode());
        apiResponse.setMessage(e.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
