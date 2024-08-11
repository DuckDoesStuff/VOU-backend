package com.example.GameService.service;

// GameService.java
import com.example.GameService.dto.ApiResponse;
import com.example.GameService.dto.GetGameRequestDTO;
import com.example.GameService.entity.Game;
import com.example.GameService.repository.GameRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public ResponseEntity<ApiResponse<Game>> getGame(GetGameRequestDTO gameRequestDTO) {
        List<Game> games = gameRepository.findByEventIDAndGameID(
                gameRequestDTO.getEventID(),
                gameRequestDTO.getGameID());
        if (!games.isEmpty()) {
            ApiResponse<Game> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "",
                    games.getFirst()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponse<Game> response = new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "Cannot get game",
            null
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<ApiResponse<Game>> createGame(Game game) {
        if (game.getGameID() == null) {
            game.setGameID(new ObjectId());
        }

        Game savedGame = gameRepository.save(game);

        ApiResponse<Game> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Game created successfully",
                savedGame
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<String>> deleteGame(GetGameRequestDTO getGameRequestDTO) {
        long count = gameRepository.deleteByGameIDAndEventID(
                getGameRequestDTO.getGameID(),
                getGameRequestDTO.getEventID()
        );
        ApiResponse<String> response = new ApiResponse<>();
        if (count == 0) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Cannot delete this game");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

