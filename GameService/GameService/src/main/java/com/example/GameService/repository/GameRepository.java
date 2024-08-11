package com.example.GameService.repository;

// GameRepository.java
import com.example.GameService.entity.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByEventIDAndGameID(Long eventID, ObjectId gameID);
    @Query(value="{ 'gameID': ?0, 'eventID': ?1 }", delete = true)
    long deleteByGameIDAndEventID(ObjectId gameID, Long eventID);

}
