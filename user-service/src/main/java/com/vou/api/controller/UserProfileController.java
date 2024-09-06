package com.vou.api.controller;

import com.vou.api.dto.request.ActivateProfileRequest;
import com.vou.api.dto.response.ApiResponse;
import com.vou.api.dto.response.AuthRegisterResponse;
import com.vou.api.dto.user.UserRegisterDto;
import com.vou.api.dto.user.UserUpdateDto;
import com.vou.api.entity.UserProfile;
import com.vou.api.enumerate.ProfileState;
import com.vou.api.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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

    @GetMapping("/search")
    public ApiResponse<List<UserProfile>> getUserMatchUsername(@RequestParam("username") String username) {
        return ApiResponse.<List<UserProfile>>builder()
                .code(200)
                .result(userProfileService.getUserMatchUsername(username))
                .build();
    }

    @PostMapping
    public ApiResponse<AuthRegisterResponse> createUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
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
    public ApiResponse<UserProfile> updateUserProfile(@RequestBody UserUpdateDto userUpdateDto, @PathVariable UUID id){
        return ApiResponse.<UserProfile>builder()
                .code(200)
                .result(userProfileService.updateUser(id, userUpdateDto))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUserProfile(@PathVariable UUID id) {
        return ApiResponse.<String>builder()
                .code(200)
                .result(userProfileService.deleteUser(id))
                .build();
    }

    @KafkaListener(topics = "user-profile-topic")
    public void listener(ActivateProfileRequest activateProfileRequest) {
        userProfileService.verifyUser(activateProfileRequest.getName(), activateProfileRequest.getProfileState());
    }
}
