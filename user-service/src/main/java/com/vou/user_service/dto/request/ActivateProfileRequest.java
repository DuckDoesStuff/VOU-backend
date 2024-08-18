package com.vou.user_service.dto.request;

import com.vou.user_service.enumerate.ProfileState;
import lombok.Data;

@Data
public class ActivateProfileRequest {
    private String name;
    private ProfileState profileState;
}
