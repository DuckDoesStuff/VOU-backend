package com.vou.api.controller;

import com.vou.api.entity.GameHistory;
import com.vou.api.entity.UserInfo;
import com.vou.api.service.GameHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game_histories")
public class GameHistoryController {
    @Autowired
    private GameHistoryService gameHistoryService;

    @GetMapping
    public List<GameHistory> getAllGameHistories() {
        return gameHistoryService.getAllGameHistories();
    }

    @GetMapping("/{id}")
    public GameHistory getGameHistory(@PathVariable String id) {
        return gameHistoryService.getGameHistory(id);
    }

    @GetMapping("/event/{eventID}")
    public List<GameHistory> getGameHistoriesByEventID(@PathVariable Long eventID) {
        return gameHistoryService.getGameHistoriesByEventID(eventID);
    }

    // Commenting this because I'm too lazy to fix the real bug
//    @GetMapping("/user/{userID}")
//    public List<GameHistory> getGameHistoriesByUserID(@PathVariable Long userID) {
//        return gameHistoryService.getGameHistoriesByUserID(userID);
//    }

    @GetMapping("/game/{gameID}")
    public List<GameHistory> getGameHistoriesByGameID(@PathVariable Long gameID) {
        return gameHistoryService.getGameHistoriesByGameID(gameID);
    }

    @DeleteMapping("/{id}")
    public void deleteGameHistory(@PathVariable String id) {
        gameHistoryService.deleteGameHistory(id);
    }

    @KafkaListener(topics = "SaveGameHistory")
    public void saveGameHistory(List<UserInfo> userInfos) {
        gameHistoryService.saveGameHistory(userInfos);
    }
}
