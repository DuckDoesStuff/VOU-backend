package com.vou.api.repository;

import com.vou.api.entity.GameStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStatisticsRepository extends JpaRepository<GameStatistics, String> {
    @Query("SELECT SUM(ga.totalParticipants) FROM game_statistics ga")
    Long findTotalParticipants();

    @Query("SELECT COUNT(gameID) FROM game_statistics ga")
    Long findTotalGame();
}
