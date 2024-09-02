package com.vou.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGamePlaytime {
    private String userID;
    private String gameID;
    private Long totalPlaytime;
    private LocalDateTime joinTime;
    private LocalDateTime leftTime;

    public UserGamePlaytime(String userID, String gameID, LocalDateTime joinTime, LocalDateTime leftTime) {
        this.userID = userID;
        this.gameID = gameID;
        this.joinTime = joinTime;
        this.leftTime = leftTime;
        this.totalPlaytime = calculatePlaytimeInSeconds();
    }

    private Long calculatePlaytimeInSeconds() {
        return Duration.between(joinTime, leftTime).getSeconds();
    }
}
