package com.example.demo.controller;

import com.example.demo.dto.ProfileStateDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.entity.UserProfile;
import com.example.demo.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public List<UserProfile> getAllUsers() {
        return userProfileService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserProfile getUserById(@PathVariable Long id) {
        return userProfileService.getUserById(id);
    }

    @PostMapping
    public String createUser(@RequestBody RegisterDto registerDto) {
        return userProfileService.createUser(registerDto);
    }

    @PostMapping("/{username}")
    public UserProfile changeProfileState(@PathVariable String username, @RequestBody ProfileStateDto profileStateDto) {
        return userProfileService.changeProfileState(username, profileStateDto);
    }

    @PutMapping("/{id}")
    public UserProfile updateUser(@PathVariable Long id, @RequestBody UserProfile userProfileDetails) {
        return userProfileService.updateUser(id, userProfileDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userProfileService.deleteUser(id);
    }

//    @PutMapping("/{id}/activate")
//    public UserProfile activateUser(@PathVariable Long id) {
//        return userProfileService.activateUser(id);
//    }
//
//    @PutMapping("/{id}/deactivate")
//    public UserProfile deactivateUser(@PathVariable Long id) {
//        return userProfileService.deactivateUser(id);
//    }
}
