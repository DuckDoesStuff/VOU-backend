package com.example.GameService.repository;

// GameRepository.java
import com.example.GameService.entity.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByEventIDAndGameID(Long eventID, Long gameID);
}
