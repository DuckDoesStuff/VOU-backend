package com.vou.api.dto.request;

import com.vou.api.enumerate.ProfileState;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivateProfileRequest {
    private String name;
    private ProfileState profileState;
}
