package com.vou.api.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@Builder
public class UserUpdateDto {
    private Optional<String> displayName = Optional.empty();
    private Optional<String> phone = Optional.empty();
    private Optional<String> email = Optional.empty();
    private Optional<String> avatar = Optional.empty();
}
