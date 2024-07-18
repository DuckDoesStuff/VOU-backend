package com.vou.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
public class AuthResponse {
    private String token;
    private String refreshToken;
    private Boolean authorized;
}
