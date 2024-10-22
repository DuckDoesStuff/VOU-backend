package com.vou.api.controller;

// GameController.java

import com.vou.api.dto.ApiResponse;
import com.vou.api.entity.Game;
import com.vou.api.service.GameService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/game/event/{gameID}/{eventID}")
    public ResponseEntity<ApiResponse<Game>> getGame(@PathVariable String gameID, @PathVariable Long eventID) {
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

    @GetMapping("/game/event/{eventID}")
    public ResponseEntity<ApiResponse<List<Game>>> getGamesByEventID(@PathVariable Long eventID) {
        return gameService.getGameByEventID(eventID);
    }

    @PutMapping("/game/{gameID}")
    public ResponseEntity<ApiResponse<Game>> updateGame(
            @PathVariable ObjectId gameID,
            @RequestBody Game updatedGameDetails) {
        return gameService.updateGame(gameID, updatedGameDetails);
    }

    @PostMapping("/game/question")
    public ResponseEntity<ApiResponse<Game>> addGameQuestion(@RequestParam ObjectId gameID, @RequestBody Game.Question questionDto) {
        return gameService.addGameQuestion(gameID, questionDto);
    }

    @GetMapping("/game/ready")
    public ResponseEntity<ApiResponse<List<Game>>> getGameReadyToStream() {
        return gameService.getGameReadyToStream();
    }
}
