package com.vou.api.dto.user;

import com.vou.api.enumerate.ProfileState;
import com.vou.api.enumerate.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@Builder
public class UserUpdateDto {
    private String displayName;
    private String phone;
    private String email;
    private LocalDate birthday;
    private String avatar;
    private String gender;
    private ProfileState state;
    private Role role;
}
