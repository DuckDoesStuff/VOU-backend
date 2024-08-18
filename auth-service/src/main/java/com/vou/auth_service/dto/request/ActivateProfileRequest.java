package com.vou.auth_service.dto.request;

import com.vou.auth_service.enumerate.ProfileState;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivateProfileRequest {
    private String name;
    private ProfileState profileState;
}
