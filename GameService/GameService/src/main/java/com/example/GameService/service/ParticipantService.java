package com.example.GameService.service;

// ParticipantService.java
import com.example.GameService.entity.Participant;
import com.example.GameService.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public Participant getParticipant(String id) {
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
        participantRepository.deleteById(id);
    }
}

