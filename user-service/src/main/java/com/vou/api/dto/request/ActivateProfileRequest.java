package com.vou.api.dto.request;

import com.vou.api.enumerate.ProfileState;
import lombok.Data;

@Data
public class ActivateProfileRequest {
    private String name;
    private ProfileState profileState;
}
