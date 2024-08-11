package com.example.GameService.controller;

// GameController.java
import com.example.GameService.dto.ApiResponse;
import com.example.GameService.dto.GetGameRequestDTO;
import com.example.GameService.entity.Game;
import com.example.GameService.service.GameService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/game/event")
    public ResponseEntity<ApiResponse<Game>> getGame(@RequestBody GetGameRequestDTO gameRequestDTO) {
        return gameService.getGame(gameRequestDTO);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Game>> createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    @DeleteMapping("/game/event")
    public ResponseEntity<ApiResponse<String>> deleteGame(@RequestBody GetGameRequestDTO gameRequestDTO) {
        return gameService.deleteGame(gameRequestDTO);
    }
}
