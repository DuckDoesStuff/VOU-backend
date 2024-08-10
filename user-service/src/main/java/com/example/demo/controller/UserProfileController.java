package com.example.demo.controller;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.AuthRegisterResponse;
import com.example.demo.dto.user.UserRegisterDto;
import com.example.demo.dto.user.UserUpdateDto;
import com.example.demo.entity.UserProfile;
import com.example.demo.enumerate.ProfileState;
import com.example.demo.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile/user")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public ApiResponse<List<UserProfile>> getAllUsers() {
        return ApiResponse.<List<UserProfile>>builder()
                .code(200)
                .result(userProfileService.getAllUsers())
                .build();
    }

    @PostMapping
    public ApiResponse<AuthRegisterResponse> createUser(@RequestBody UserRegisterDto userRegisterDto) {
        AuthRegisterResponse authRegisterResponse = userProfileService.createUser(userRegisterDto);
        return ApiResponse.<AuthRegisterResponse>builder()
                .code(200)
                .result(authRegisterResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserProfile> getUserById(@PathVariable UUID id) {
        return ApiResponse.<UserProfile>builder()
                .code(200)
                .result(userProfileService.getUserById(id))
                .build();
    }

    @PatchMapping("/{username}")
    public UserProfile verifyUser(@PathVariable String username) {
        return userProfileService.verifyUser(username, ProfileState.VERIFIED);
    }

    @PutMapping("/{id}")
    public ApiResponse<UserProfile> updateUserProfile(@RequestBody UserUpdateDto userUpdateDto, UUID id) {
        return ApiResponse.<UserProfile>builder()
                .code(200)
                .result(userProfileService.updateUser(id, userUpdateDto))
                .build();
    }
}
