package com.vou.api.controller;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ReportTotalParticipantsByBrand;
import com.vou.api.dto.ReportUserCount;
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

    // Play time of all users
    @GetMapping("/user-activity/play-time")
    public List<UserGamePlaytime> getUsersPlayTime() {
        return userActivityService.getUsersPlayTime();
    }

    // Total user (old + new) sine last week
    @GetMapping("/user-activity/user-count")
    public ApiResponse<ReportUserCount> reportUserCountSinceLastWeek() {
        return userActivityService.reportNumUsersSinceLastWeek();
    }

    // Total user (old + new, historical data) of a brand
    @GetMapping("/user-activity/participants/total-by-brand")
    public ApiResponse<List<ReportTotalParticipantsByBrand>> getTotalParticipantsByBrand() {
        return userActivityService.getTotalParticipantsByBrand();
    }
}
