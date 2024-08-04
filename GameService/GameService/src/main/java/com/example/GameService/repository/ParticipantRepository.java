package com.example.GameService.repository;

// ParticipantRepository.java
import com.example.GameService.entity.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ParticipantRepository extends MongoRepository<Participant, String> {
    List<Participant> findByEventID(Long eventID);
    List<Participant> findByUserID(Long userID);
    List<Participant> findByGameID(Long gameID);
}

