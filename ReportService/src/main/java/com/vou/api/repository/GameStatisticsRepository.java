package com.vou.api.repository;

import com.vou.api.entity.GameStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStatisticsRepository extends JpaRepository<GameStatistics, String> {

    @Query("SELECT COALESCE(SUM(ga.totalParticipants), 0) FROM GameStatistics ga")
    Long findTotalParticipants();

    @Query("SELECT COUNT(ga.gameID) FROM GameStatistics ga")
    Long findTotalGame();
}
