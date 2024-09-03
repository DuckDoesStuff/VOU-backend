package com.vou.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityMessage {
    private String userID;
    private Long eventID;
    private String gameID;
    private LocalDateTime joinTime;
    private LocalDateTime leftTime;
    private String activityType;
    private String gameType;
    private String rewardType;
}