package com.example.GameService.repository;

// GameHistoryRepository.java
import com.example.GameService.entity.GameHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameHistoryRepository extends MongoRepository<GameHistory, String> {
    List<GameHistory> findByEventID(Long eventID);
    List<GameHistory> findByUserID(Long userID);
    List<GameHistory> findByGameID(Long gameID);
}
