package com.example.ReportService.repository;


import com.example.ReportService.entity.UserActivity;
import com.example.ReportService.dto.UserGamePlaytime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    @Query("SELECT new com.example.ReportService.dto.UserGamePlaytime(" +
            "ua.userID, " +
            "ua.gameID, " +
            "SUM(CAST(TIMESTAMPDIFF(SECOND, ua.joinTime, ua.leftTime) AS long))) " +
            "FROM UserActivity ua " +
            "GROUP BY ua.userID, ua.gameID")
    List<UserGamePlaytime> findTotalPlaytimeByUserAndGame();



}
