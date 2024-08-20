package com.example.GameService.repository;

// GameRepository.java
import com.example.GameService.entity.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
    List<Game> findByEventIDAndGameID(Long eventID, ObjectId gameID);

    @Query(value = "{ 'gameID': ?0, 'eventID': ?1 }", delete = true)
    long deleteByGameIDAndEventID(ObjectId gameID, Long eventID);

    Game findByGameID(ObjectId gameID);

    @Query("{ 'eventID': { $in: ?0 } }")
    List<Game> findGamesByEventIDs(List<Long> eventIDs);

    @Query("{ 'eventID': ?0 }")
    List<Game> findGamesByEventID(@Param("eventID") Long eventID);
}
