package com.vou.api.service;

// GameHistoryService.java

import com.vou.api.entity.GameHistory;
import com.vou.api.entity.UserInfo;
import com.vou.api.repository.GameHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void saveGameHistory(List<UserInfo> userInfos) {
        GameHistory gameHistory = new GameHistory();
        gameHistory.setGameID(userInfos.getFirst().getGameID());
        gameHistory.setEventID(userInfos.getFirst().getEventID());

        List<GameHistory.UserInfo> gameHistoryUserInfo = new ArrayList<>();
        for (UserInfo userInfo : userInfos)
            gameHistoryUserInfo.add(GameHistory.UserInfo.builder()
                    .userID(userInfo.getUserID())
                    .joinTime(userInfo.getJoinTime())
                    .leftTime(userInfo.getLeaveTime())
                    .build());

        gameHistory.setUsers(gameHistoryUserInfo);
    }

    public void deleteGameHistory(String id) {
        gameHistoryRepository.deleteById(id);
    }
}

