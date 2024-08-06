package com.example.demo.controller;

import com.example.demo.dto.ProfileStateDto;
import com.example.demo.dto.UserRegisterDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.AuthRegisterResponse;
import com.example.demo.entity.UserProfile;
import com.example.demo.enumerate.ProfileState;
import com.example.demo.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public List<UserProfile> getAllUsers() {
        return userProfileService.getAllUsers();
    }

    @PostMapping
    public ApiResponse<AuthRegisterResponse> createUser(@RequestBody UserRegisterDto userRegisterDto) {
        AuthRegisterResponse authRegisterResponse = userProfileService.createUser(userRegisterDto);
        return ApiResponse.<AuthRegisterResponse>builder()
                .code(200)
                .result(authRegisterResponse)
                .build();
    }

    @PutMapping("/{username}")
    public UserProfile verifyUser(@PathVariable String username) {
        return userProfileService.verifyUser(username, ProfileState.VERIFIED);
    }

    @GetMapping("/{id}")
    public UserProfile getUserById(@PathVariable UUID id) {
        return userProfileService.getUserById(id);
    }

//    @PutMapping("/{id}")
//    public UserProfile updateUser(@PathVariable UUID id, @RequestBody UserProfile userProfileDetails) {
//        return userProfileService.updateUser(id, userProfileDetails);
//    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userProfileService.deleteUser(id);
    }
}
