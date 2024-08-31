package com.vou.api.service;

import com.vou.api.dto.UserGamePlaytime;
import com.vou.api.repository.UserActivityRepository;
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
