package com.example.demo.controller;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Friend;
import com.example.demo.entity.UserProfile;
import com.example.demo.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/profile/friend")
public class FriendController {
    @Autowired
    FriendService friendService;

    @PostMapping("/{username}")
    public ApiResponse<Friend> addFriend(@PathVariable String username, @RequestParam(name = "name", defaultValue = "") String friendname) {
        return ApiResponse.<Friend>builder()
                .result(friendService.addFriend(username, friendname))
                .code(200)
                .build();
    }

    @GetMapping("/{username}")
    public ApiResponse<List<UserProfile>> getFriends(@PathVariable String username) {
        return ApiResponse.<List<UserProfile>>builder()
                .result(friendService.getFriends(username))
                .code(200)
                .build();
    }
}
