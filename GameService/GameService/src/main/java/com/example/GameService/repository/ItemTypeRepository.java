package com.example.GameService.repository;

// ItemTypeRepository.java
import com.example.GameService.entity.ItemType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ItemTypeRepository extends MongoRepository<ItemType, ObjectId> {
    List<ItemType> findByEventID(Long eventID);
    List<ItemType> findByGameID(ObjectId gameID);
    List<ItemType> findByType(String type);
}
