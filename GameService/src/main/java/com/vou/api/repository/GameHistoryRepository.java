package com.vou.api.repository;

// GameHistoryRepository.java
import com.vou.api.entity.GameHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameHistoryRepository extends MongoRepository<GameHistory, String> {
    List<GameHistory> findByEventID(Long eventID);
    List<GameHistory> findByUserID(Long userID);
    List<GameHistory> findByGameID(Long gameID);
}
