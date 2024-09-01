package com.vou.api.repository;


import com.vou.api.entity.UserActivity;
import com.vou.api.dto.UserGamePlaytime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    @Query("SELECT new com.vou.api.dto.UserGamePlaytime(" +
            "ua.userID, " +
            "ua.gameID, " +
            "SUM(CAST(TIMESTAMPDIFF(SECOND, ua.joinTime, ua.leftTime) AS long))) " +
            "FROM UserActivity ua " +
            "GROUP BY ua.userID, ua.gameID")
    List<UserGamePlaytime> findTotalPlaytimeByUserAndGame();



}
