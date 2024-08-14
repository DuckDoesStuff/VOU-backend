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
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping()
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Game>>> getAllGames() {
        return gameService.getAllGames();
    }
    // Delete by url not by id
    @GetMapping("/game/event/{gameID}/{eventID}")
    public ResponseEntity<ApiResponse<Game>> getGame(@PathVariable ObjectId gameID, @PathVariable Long eventID) {
        return gameService.getGame(gameID, eventID);
    }
    @GetMapping("/brand/{brandID}")
    public ResponseEntity<ApiResponse<List<Game>>> getGame(@PathVariable Long brandID) {
        return gameService.getGamesByBrandID(brandID);
    }

    @PostMapping("/game")
    public ResponseEntity<ApiResponse<Game>> createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    @DeleteMapping("/game/event/{gameID}/{eventID}")
    public ResponseEntity<ApiResponse<String>> deleteGame(@PathVariable ObjectId gameID, @PathVariable Long eventID) {
        return gameService.deleteGame(gameID, eventID);
    }
    @PutMapping("/game/{gameID}")
    public ResponseEntity<ApiResponse<Game>> updateGame(
            @PathVariable String gameID,
            @RequestBody Game updatedGameDetails) {
        return gameService.updateGame(new ObjectId(gameID), updatedGameDetails);
    }
}
