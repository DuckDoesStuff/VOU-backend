package com.example.ReportService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "user_activity")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long activityID;
    private String userID;
    private String gameID;
    private Long eventID;
    private LocalDateTime joinTime;
    private LocalDateTime leftTime;
    private String gameType;
    private String rewardType;
}
