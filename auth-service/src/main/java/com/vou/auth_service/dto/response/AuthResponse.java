package com.vou.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String profileID;
    private String role;
    private Boolean authorized;
}
