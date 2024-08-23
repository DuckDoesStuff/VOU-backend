package com.vou.api.service;

// GameHistoryService.java
import com.vou.api.entity.GameHistory;
import com.vou.api.repository.GameHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameHistoryService {
    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    public List<GameHistory> getAllGameHistories() {
        return gameHistoryRepository.findAll();
    }

    public GameHistory getGameHistory(String id) {
        return gameHistoryRepository.findById(id).orElse(null);
    }

    public List<GameHistory> getGameHistoriesByEventID(Long eventID) {
        return gameHistoryRepository.findByEventID(eventID);
    }

    public List<GameHistory> getGameHistoriesByUserID(Long userID) {
        return gameHistoryRepository.findByUserID(userID);
    }

    public List<GameHistory> getGameHistoriesByGameID(Long gameID) {
        return gameHistoryRepository.findByGameID(gameID);
    }

    public GameHistory saveGameHistory(GameHistory gameHistory) {
        return gameHistoryRepository.save(gameHistory);
    }

    public void deleteGameHistory(String id) {
        gameHistoryRepository.deleteById(id);
    }
}

