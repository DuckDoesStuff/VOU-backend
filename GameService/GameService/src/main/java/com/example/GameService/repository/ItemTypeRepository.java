package com.example.GameService.repository;

// ItemTypeRepository.java
import com.example.GameService.entity.ItemType;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ItemTypeRepository extends MongoRepository<ItemType, String> {
    List<ItemType> findByEventID(Long eventID);
    List<ItemType> findByGameID(Long gameID);
    List<ItemType> findByType(String type);
}
