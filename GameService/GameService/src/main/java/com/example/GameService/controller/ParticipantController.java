package com.example.GameService.controller;

// ParticipantController.java
import com.example.GameService.entity.Participant;
import com.example.GameService.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Participant getParticipant(@PathVariable String id) {
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

    @PostMapping
    public Participant saveParticipant(@RequestBody Participant participant) {
        return participantService.saveParticipant(participant);
    }

    @DeleteMapping("/{id}")
    public void deleteParticipant(@PathVariable String id) {
        participantService.deleteParticipant(id);
    }
}
