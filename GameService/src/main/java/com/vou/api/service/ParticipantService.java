package com.vou.api.service;

// ParticipantService.java
import com.vou.api.dto.AddParticipantRequest;
import com.vou.api.dto.ApiResponse;
import com.vou.api.entity.Game;
import com.vou.api.entity.Participant;
import com.vou.api.repository.GameRepository;
import com.vou.api.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Participant participant = new Participant();

        participant.setGameID(addParticipantRequest.getGameID());
        participant.setEventID(addParticipantRequest.getEventID());
        participant.setUserID(addParticipantRequest.getUserID());
        Game game = gameRepository.findByGameID(addParticipantRequest.getGameID());
        participant.setTurnLeft(game.getDefaultFreeTurn());
        // Send kafka message to report service for tracking their first time join game
        if (game.getDefaultFreeTurn() == 1) {
            // quiz game
            kafkaService.sendUserJoinQuizGame(participant);
        } else {
            // shake game
            kafkaService.sendUserJoinQuizGame(participant);
        }

        try {
            Participant savedParticipant = participantRepository.save(participant);
            ApiResponse<Participant> response = new ApiResponse<>(200, "Participant added successfully", savedParticipant);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Participant> response = new ApiResponse<>(500, "An error occurred while saving the participant", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

