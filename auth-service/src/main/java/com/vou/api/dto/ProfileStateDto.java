package com.vou.api.dto;

import com.vou.api.enumerate.ProfileState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileStateDto {
    private ProfileState state;
}
