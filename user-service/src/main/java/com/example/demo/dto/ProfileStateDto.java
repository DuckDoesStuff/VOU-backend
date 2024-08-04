package com.example.demo.dto;

import com.example.demo.enumerate.ProfileState;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileStateDto {
    @NotNull(message = "A state must be specified")
    private ProfileState state;
}
