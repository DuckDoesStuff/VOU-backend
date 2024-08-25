package com.example.ReportService.service;

import com.example.ReportService.dto.UserGamePlaytime;
import com.example.ReportService.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserActivityRepository userActivityRepository;

    public List<UserGamePlaytime> getUsersPlayTime() {
        return userActivityRepository.findTotalPlaytimeByUserAndGame();
    }
}
