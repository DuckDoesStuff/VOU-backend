package com.vou.user_service.controller;

import com.vou.user_service.dto.response.ApiResponse;
import com.vou.user_service.entity.Friend;
import com.vou.user_service.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<List<Friend>> getFriends(@PathVariable String username) {
        return ApiResponse.<List<Friend>>builder()
                .result(friendService.getFriends(username))
                .code(200)
                .build();
    }
}
