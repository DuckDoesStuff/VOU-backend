package com.vou.api.service;

// GameService.java

import com.vou.api.custom.HeyGenAPI;
import com.vou.api.dto.ApiResponse;
import com.vou.api.entity.Game;
import com.vou.api.repository.GameRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    HeyGenAPI heyGenAPI;

    private final RestTemplate restTemplate;

    public GameService(RestTemplate restTemplate) {
        this.restTemplate   = restTemplate;
    }

    public ResponseEntity<ApiResponse<List<Game>>> getAllGames() {
        List<Game> games = gameRepository.findAll();
        ApiResponse<List<Game>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "",
                games
        );
//        if (games.isEmpty()) {
//            response.setStatus(HttpStatus.NOT_FOUND.value());
//            response.setMessage("Cannot find games");
//            response.setResult(null);
//            return new ResponseEntity<>(
//                    response,
//                    HttpStatus.NOT_FOUND
//            );
//        }

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public ResponseEntity<ApiResponse<Game>> getGame(String gameID, Long eventID) {
        List<Game> games = gameRepository.findByEventIDAndGameID(
                eventID,
                gameID);
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

    public ResponseEntity<ApiResponse<List<Game>>> getGamesByBrandID(Long brandID) {
        String url = "http://localhost:8003/events/brand/" + brandID;
        ResponseEntity<ApiResponse<List<Long>>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<List<Long>>>() {
                }
        );
        ApiResponse<List<Game>> response;
        if (responseEntity.getBody() == null) {
            response = new ApiResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    "No games found for the given brandID",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        List<Long> eventIDs = responseEntity.getBody().getResult();
        List<Game> games = gameRepository.findGamesByEventIDs(eventIDs);

        if (!games.isEmpty()) {
            response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Games retrieved successfully",
                    games
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ApiResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    "No games found for the given brandID",
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<ApiResponse<Game>> createGame(Game game) {
        if (game.getGameID() == null) {
            game.setGameID(new ObjectId());
        }


        if (game.getType().equals("QUIZ")) {
            game.setQuestions(new ArrayList<>());
            game.setQuizState("PREPARING");
        }
        Game savedGame = gameRepository.save(game);

        ApiResponse<Game> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Game created successfully",
                savedGame
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<Game>> updateGame(ObjectId gameID, Game updatedGameDetails) {
        Game existingGame = gameRepository.findByGameID(gameID);
        ApiResponse<Game> response = new ApiResponse<>();

        if (existingGame != null) {
            existingGame.setNameOfGame(updatedGameDetails.getNameOfGame());
            existingGame.setPicture(updatedGameDetails.getPicture());
            existingGame.setType(updatedGameDetails.getType());
            existingGame.setInstruction(updatedGameDetails.getInstruction());
            existingGame.setDefaultFreeTurn(updatedGameDetails.getDefaultFreeTurn());
            existingGame.setStartTime(updatedGameDetails.getStartTime());
            existingGame.setEndTime(updatedGameDetails.getEndTime());

            Game savedGame = gameRepository.save(existingGame);

            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Game updated successfully");
            response.setResult(savedGame);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Game not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ApiResponse<List<Game>>> getGameByEventID(Long eventID) {
        List<Game> games = gameRepository.findGamesByEventID(
                eventID
        );
        ApiResponse<List<Game>> response = new ApiResponse<>();
//        if (count == 0) {
//            response.setStatus(HttpStatus.NOT_FOUND.value());
//            response.setMessage("Cannot delete this game");
//            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
//        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get successfully");
        response.setResult(games);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<Game>> addGameQuestion(ObjectId gameID, Game.Question question) {
        Game game = gameRepository.findByGameID(gameID);
        List<Game.Question> questions = game.getQuestions();
        if (questions == null) questions = new ArrayList<>();

        String videoID = heyGenAPI.generateVideo(question.getQuestion(), game.getEventID().toString(), gameID.toString(), String.valueOf(questions.size()));
        question.setVideoStatus("processing");
        question.setVideo(videoID);

        questions.add(question);
        game.setQuestions(questions);
        game.setQuizState("PREPARING");
        gameRepository.save(game);
        ApiResponse<Game> response = new ApiResponse<>();
        response.setMessage("Successfully added a new question");
        response.setStatus(200);
        response.setResult(game);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
}

