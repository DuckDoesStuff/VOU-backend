package com.example.GameService.service;

// GameService.java
import com.example.GameService.entity.Game;
import com.example.GameService.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGame(Long eventID, Long gameID) {
        List<Game> games = gameRepository.findByEventIDAndGameID(eventID, gameID);
        return games.isEmpty() ? null : games.get(0);
    }

    public Game saveGame(Game game) {
        System.out.println(game);
        return gameRepository.save(game);
    }

    public void deleteGame(Long eventID, Long gameID) {
        List<Game> games = gameRepository.findByEventIDAndGameID(eventID, gameID);
        if (!games.isEmpty()) {
            gameRepository.delete(games.get(0));
        }
    }
}

