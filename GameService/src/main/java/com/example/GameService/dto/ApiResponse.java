package com.example.GameService.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T result;

    public ApiResponse(int status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }
}
