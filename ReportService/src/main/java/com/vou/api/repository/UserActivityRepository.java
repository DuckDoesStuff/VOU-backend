package com.vou.api.repository;

import com.vou.api.dto.ReportTotalParticipantsByBrand;
import com.vou.api.dto.UserGamePlaytime;
import com.vou.api.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    @Query("SELECT new com.vou.api.dto.UserGamePlaytime(ua.userID, ua.gameID, ua.joinTime, ua.leftTime) " +
            "FROM UserActivity ua " +
            "GROUP BY ua.userID, ua.gameID, ua.joinTime, ua.leftTime")
    List<UserGamePlaytime> findTotalPlaytimeByUserAndGame();


    @Query("SELECT COUNT(DISTINCT ua.userID) " +
            "FROM UserActivity ua " +
            "WHERE ua.joinTime >= :lastWeek")
    long countNewUsersSinceLastWeek(@Param("lastWeek") LocalDateTime lastWeek);

    @Query("SELECT COUNT(DISTINCT ua.userID) " +
            "FROM UserActivity ua " +
            "WHERE ua.joinTime < :lastWeek")
    long countOldUsersSinceLastWeek(@Param("lastWeek") LocalDateTime lastWeek);


    @Query("SELECT new com.vou.api.dto.ReportTotalParticipantsByBrand(event.brandID, COUNT(DISTINCT ua.userID)) " +
            "FROM UserActivity ua " +
            "JOIN PromotionalEvent event ON event.eventID = ua.eventID " +
            "WHERE ua.activityType = 'join_game' " +
            "GROUP BY event.brandID")
    List<ReportTotalParticipantsByBrand> findTotalParticipantsByBrand();

    @Query("SELECT " +
            "SUM(CASE WHEN ua.joinTime >= :lastWeek THEN 1 ELSE 0 END) AS newUsers, " +
            "SUM(CASE WHEN ua.joinTime < :lastWeek THEN 1 ELSE 0 END) AS oldUsers " +
            "FROM UserActivity ua " +
            "JOIN PromotionalEvent pe ON pe.eventID = ua.eventID " +
            "WHERE pe.brandID = :brandID")
    Object[] countNewAndOldUsersByBrand(@Param("lastWeek") LocalDateTime lastWeek, @Param("brandID") String brandID);
}
