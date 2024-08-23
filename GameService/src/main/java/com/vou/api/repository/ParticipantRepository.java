package com.vou.api.repository;

// ParticipantRepository.java
import com.vou.api.entity.Participant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends MongoRepository<Participant, ObjectId> {
    List<Participant> findByEventID(Long eventID);
    List<Participant> findByUserID(Long userID);
    List<Participant> findByGameID(Long gameID);
    @Query("{ 'eventID': ?0, 'gameID': ?1, 'userID': ?2 }")
    Optional<Participant> findParticipantByEventGameAndUser(Long eventID, ObjectId gameID, String userID);

}

