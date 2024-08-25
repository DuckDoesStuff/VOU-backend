package com.example.ReportService.controller;

import com.example.ReportService.dto.UserGamePlaytime;
import com.example.ReportService.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
