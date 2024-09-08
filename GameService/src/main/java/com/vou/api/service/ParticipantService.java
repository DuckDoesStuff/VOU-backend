package com.vou.api.service;

// ParticipantService.java
import com.vou.api.dto.AddParticipantRequest;
import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ShareTurnForFriendRequest;
import com.vou.api.entity.Game;
import com.vou.api.entity.Participant;
import com.vou.api.repository.GameRepository;
import com.vou.api.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final GameRepository gameRepository;
    private final KafkaService<Object> kafkaService;

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public Participant getParticipant(ObjectId id) {
        return participantRepository.findById(id).orElse(null);
    }

    public List<Participant> getParticipantsByEventID(Long eventID) {
        return participantRepository.findByEventID(eventID);
    }

    public List<Participant> getParticipantsByUserID(Long userID) {
        return participantRepository.findByUserID(userID);
    }

    public List<Participant> getParticipantsByGameID(Long gameID) {
        return participantRepository.findByGameID(gameID);
    }
    public Participant saveParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public void deleteParticipant(String id) {

    }
    public ResponseEntity<ApiResponse<Participant>> addParticipantToGame(AddParticipantRequest addParticipantRequest) {
        // Check if the participant already exists in the database
        Optional<Participant> existingParticipant = participantRepository.findParticipantByEventGameAndUser(
                addParticipantRequest.getEventID(),
                addParticipantRequest.getGameID(),
                addParticipantRequest.getUserID());

        // If the participant already exists, return an error response
        if (existingParticipant.isPresent()) {
            ApiResponse<Participant> response = new ApiResponse<>(400, "User has already joined this game", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Create a new participant if no existing participant is found
        Participant participant = new Participant();
        participant.setGameID(addParticipantRequest.getGameID());
        participant.setEventID(addParticipantRequest.getEventID());
        participant.setUserID(addParticipantRequest.getUserID());

        Game game = gameRepository.findByGameID(addParticipantRequest.getGameID());
        participant.setTurnLeft(game.getDefaultFreeTurn());

        // Send kafka message to report service for tracking their first time join game
        if (game.getDefaultFreeTurn() == 1) {
            kafkaService.sendUserJoinQuizGame(participant);
        } else {
            kafkaService.sendUserJoinShakeGame(participant);
        }

        try {
            // Save the participant to the database
            Participant savedParticipant = participantRepository.save(participant);
            ApiResponse<Participant> response = new ApiResponse<>(200, "Participant added successfully", savedParticipant);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any exceptions that occur while saving the participant
            ApiResponse<Participant> response = new ApiResponse<>(500, "An error occurred while saving the participant", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Boolean>> checkIfParticipantValidForShareTurn(AddParticipantRequest participantRequest) {
        Optional<Participant> participant = participantRepository.findParticipantByEventGameAndUser(
                participantRequest.getEventID(),
                participantRequest.getGameID(),
                participantRequest.getUserID()
        );
        System.out.println("check if valid sharing turn " + participantRequest);
        ApiResponse<Boolean> response = new ApiResponse<>();
        if (participant.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("This participant is not valid for sharing their turn");
            response.setResult(false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("This participant is valid for sharing their turn");
        response.setResult(true);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public ResponseEntity<ApiResponse<Boolean>> shareTurnForFriend(ShareTurnForFriendRequest request) {
        // Pariticipant a will give their turn
        Optional<Participant> participantA = participantRepository.findParticipantByEventGameAndUser(
                request.getEventID(),
                request.getGameID(),
                request.getGiver()
        );
        // Pariticipant b will increase their turn
        Optional<Participant> participantB = participantRepository.findParticipantByEventGameAndUser(
                request.getEventID(),
                request.getGameID(),
                request.getReceiver()
        );
        ApiResponse<Boolean> response = new ApiResponse<>();
        if (participantB.isEmpty() || participantA.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Participant not found");
            response.setResult(false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.NOT_FOUND
            );
        }
        int turnA = participantA.get().getTurnLeft();
        int turnB = participantB.get().getTurnLeft();
        participantA.get().setTurnLeft(turnA - 1);
        participantB.get().setTurnLeft(turnB + 1);
        participantRepository.save(participantA.get());
        participantRepository.save(participantB.get());
        response.setStatus(HttpStatus.OK.value());
        response.setResult(true);
        response.setMessage("You two have exchange turn for each other");
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
}

