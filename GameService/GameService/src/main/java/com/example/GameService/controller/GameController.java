package com.example.GameService.controller;

// GameController.java
import com.example.GameService.entity.Game;
import com.example.GameService.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{eventID}/{gameID}")
    public Game getGame(@PathVariable Long eventID, @PathVariable Long gameID) {
        return gameService.getGame(eventID, gameID);
    }

    @PostMapping
    public Game saveGame(@RequestBody Game game) {
        System.out.println(game.toString());
        return gameService.saveGame(game);
    }

    @DeleteMapping("/{eventID}/{gameID}")
    public void deleteGame(@PathVariable Long eventID, @PathVariable Long gameID) {
        gameService.deleteGame(eventID, gameID);
    }
}
