package com.vou.api.repository;

// GameRepository.java
import com.vou.api.entity.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
    List<Game> findByEventIDAndGameID(Long eventID, String gameID);

    @Query(value = "{ 'gameID': ?0, 'eventID': ?1 }", delete = true)
    long deleteByGameIDAndEventID(ObjectId gameID, Long eventID);

    Game findByGameID(ObjectId gameID);

    @Query("{ 'eventID': { $in: ?0 } }")
    List<Game> findGamesByEventIDs(List<Long> eventIDs);

    @Query("{ 'eventID': ?0 }")
    List<Game> findGamesByEventID(@Param("eventID") Long eventID);

    @Query("{ 'questions.videoStatus': ?0 }")
    List<Game> findGamesByVideoStatus(@Param("status") String status);

    @Query("{ 'quizState': 'READY', 'startTime': { $lte: ?0 }, 'endTime': { $gte: ?0 } }")
    List<Game> findGamesReadyToStream(@Param("currentTime") LocalDateTime currentTime);
}
