package com.vou.api.service;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ReportUserCount;
import com.vou.api.dto.UserGamePlaytime;
import com.vou.api.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserActivityRepository userActivityRepository;

    public List<UserGamePlaytime> getUsersPlayTime() {
        return userActivityRepository.findTotalPlaytimeByUserAndGame();
    }
    public ApiResponse<ReportUserCount> reportNumUsersSinceLastWeek() {
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        long newUsersCount = userActivityRepository.countNewUsersSinceLastWeek(lastWeek);
        long oldUsersCount = userActivityRepository.countOldUsersSinceLastWeek(lastWeek);
        ReportUserCount reportUserCount = ReportUserCount
                .builder()
                .newUsersCount(newUsersCount)
                .oldUsersCount(oldUsersCount)
                .build();
        ApiResponse<ReportUserCount> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setResult(reportUserCount);
        return response;
    }
}
