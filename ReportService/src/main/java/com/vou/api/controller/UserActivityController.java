package com.vou.api.controller;

import com.vou.api.dto.UserGamePlaytime;
import com.vou.api.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class UserActivityController {
    private final UserActivityService userActivityService;

    @GetMapping("/user_activity/play_time")
    public List<UserGamePlaytime> getUsersPlayTime() {
        return userActivityService.getUsersPlayTime();
    }
}
