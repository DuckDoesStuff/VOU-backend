package com.vou.api.service;

import com.vou.api.dto.*;
import com.vou.api.entity.UserActivity;
import com.vou.api.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
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

    public ApiResponse<List<ReportTotalParticipantsByBrand>> getTotalParticipantsByBrand() {
        List<ReportTotalParticipantsByBrand> reportTotalParticipantsByBrands =
                userActivityRepository.findTotalParticipantsByBrand();
        ApiResponse<List<ReportTotalParticipantsByBrand>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setResult(reportTotalParticipantsByBrands);
        return response;
    }

    @KafkaListener(topics = "user_join_shake_game")
    public void listenToUserJoinShakeGame(UserActivityMessage message) {
        System.out.println("Receive message join shake game" + message);
        UserActivity userActivity = UserActivity
                .builder()
                .userID(message.getUserID())
                .gameID(message.getGameID())
                .eventID(message.getEventID())
                .activityType(message.getActivityType())
                .rewardType(message.getRewardType())
                .gameType(message.getGameType())
                .joinTime(message.getJoinTime())
                .leftTime(message.getJoinTime())
                .build();
        userActivityRepository.save(userActivity);
    }

    @KafkaListener(topics = "user_join_quiz_game")
    public void listenToUserJoinQuizGame(UserActivityMessage message) {
        System.out.println("Receive message join quiz game" + message);
        UserActivity userActivity = UserActivity
                .builder()
                .userID(message.getUserID())
                .gameID(message.getGameID())
                .eventID(message.getEventID())
                .activityType(message.getActivityType())
                .rewardType(message.getRewardType())
                .gameType(message.getGameType())
                .joinTime(message.getJoinTime())
                .leftTime(message.getJoinTime())
                .build();
        userActivityRepository.save(userActivity);
    }
}
