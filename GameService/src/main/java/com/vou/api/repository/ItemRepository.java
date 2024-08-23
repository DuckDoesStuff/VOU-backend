package com.vou.api.repository;

// ItemRepository.java
import com.vou.api.entity.Item;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByEventID(Long eventID);
    List<Item> findByGameID(ObjectId gameID);
    List<Item> findByUserID(Long userID);
    List<Item> findByItemTypeID(ObjectId itemTypeID);
    Item findByEventIDAndGameIDAndItemTypeIDAndUserID(Long eventID, ObjectId gameID, ObjectId itemTypeID, Long userID);
}
