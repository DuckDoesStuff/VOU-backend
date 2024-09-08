package com.vou.api.repository;

// ItemRepository.java
import com.vou.api.dto.ItemWithDetails;
import com.vou.api.entity.Item;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends MongoRepository<Item, ObjectId> {
    List<Item> findByEventID(Long eventID);
    List<Item> findByGameID(ObjectId gameID);
    List<Item> findByUserID(String userID);
    List<Item> findByItemTypeID(ObjectId itemTypeID);
    @Query("{ 'itemTypeID': ?0, 'userID': ?1 }")
    Item findByItemTypeIDAndUserID(ObjectId itemTypeID, String userID);
    @Aggregation(pipeline = {
            "{ '$match': { 'userID': ?0, 'eventID': ?1 }}",
            "{ '$lookup': { 'from': 'item_types', 'localField': 'itemTypeID', 'foreignField': '_id', 'as': 'itemTypeDetails' }}",
            "{ '$unwind': '$itemTypeDetails' }"
    })
    List<ItemWithDetails> getItemsOfUserInAnEvent(String userID, Long eventID);
    Item findByEventIDAndGameIDAndItemTypeIDAndUserID(Long eventID, ObjectId gameID, ObjectId itemTypeID, String userID);
}
