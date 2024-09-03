package com.vou.api.controller;

// ParticipantController.java
import com.vou.api.dto.AddParticipantRequest;
import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ShareTurnForFriendRequest;
import com.vou.api.entity.Participant;
import com.vou.api.service.ParticipantService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    @GetMapping("/{id}")
    public Participant getParticipant(@PathVariable ObjectId id) {
        return participantService.getParticipant(id);
    }

    @GetMapping("/event/{eventID}")
    public List<Participant> getParticipantsByEventID(@PathVariable Long eventID) {
        return participantService.getParticipantsByEventID(eventID);
    }

    @GetMapping("/user/{userID}")
    public List<Participant> getParticipantsByUserID(@PathVariable Long userID) {
        return participantService.getParticipantsByUserID(userID);
    }

    @GetMapping("/game/{gameID}")
    public List<Participant> getParticipantsByGameID(@PathVariable Long gameID) {
        return participantService.getParticipantsByGameID(gameID);
    }

    @PostMapping("/participant")
    public ResponseEntity<ApiResponse<Participant>> addParticipantToGame(@RequestBody AddParticipantRequest addParticipantRequest) {
        return participantService.addParticipantToGame(addParticipantRequest);
    }
    @DeleteMapping("/{id}")
    public void deleteParticipant(@PathVariable String id) {
        participantService.deleteParticipant(id);
    }

    @PostMapping("/participant/valid/share-turn")
    public ResponseEntity<ApiResponse<Boolean>> checkIfParticipantValidForSharingTurn(@RequestBody AddParticipantRequest request) {
        return participantService.checkIfParticipantValidForShareTurn(request);
    }

    @PostMapping("/exchange-turn")
    public ResponseEntity<ApiResponse<Boolean>> shareTurnForFriend(ShareTurnForFriendRequest request) {
        return participantService.shareTurnForFriend(request);
    }
}
