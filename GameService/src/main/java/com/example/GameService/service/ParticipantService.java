package com.example.GameService.service;

// ParticipantService.java
import com.example.GameService.dto.AddParticipantRequest;
import com.example.GameService.dto.ApiResponse;
import com.example.GameService.entity.Game;
import com.example.GameService.entity.Participant;
import com.example.GameService.repository.GameRepository;
import com.example.GameService.repository.ParticipantRepository;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private GameRepository gameRepository;

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

