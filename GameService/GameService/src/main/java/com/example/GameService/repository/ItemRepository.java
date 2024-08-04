package com.example.GameService.repository;

// ItemRepository.java
import com.example.GameService.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByEventID(Long eventID);
    List<Item> findByGameID(Long gameID);
    List<Item> findByUserID(Long userID);
    List<Item> findByItemTypeID(Long itemTypeID);
    Item findByEventIDAndGameID(Long eventID, Long gameID);
}
