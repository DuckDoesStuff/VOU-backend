package com.vou.api.service;

// ItemService.java
import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ExchangeItemsRequest;
import com.vou.api.dto.GetRandomItemTypeDTO;
import com.vou.api.dto.ItemWithDetails;
import com.vou.api.entity.ExchangeHistory;
import com.vou.api.entity.Item;
import com.vou.api.entity.ItemType;
import com.vou.api.entity.Participant;
import com.vou.api.repository.ExchangeHistoryRepository;
import com.vou.api.repository.ItemRepository;
import com.vou.api.repository.ItemTypeRepository;
import com.vou.api.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemTypeRepository itemTypeRepository;
    private final ExchangeHistoryRepository exchangeHistoryRepository;
    private final ParticipantRepository participantRepository;
    private final KafkaService<Object> kafkaService;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItem(ObjectId id) {
        return itemRepository.findById(id).orElse(null);
    }

    public List<Item> getItemsByEventID(Long eventID) {
        return itemRepository.findByEventID(eventID);
    }

    public List<Item> getItemsByGameID(ObjectId gameID) {
        return itemRepository.findByGameID(gameID);
    }

    public ResponseEntity<ApiResponse<List<Item>>> getItemsByUserID(String userID) {
        ApiResponse<List<Item>> response = new ApiResponse<>();
        List<Item> items = itemRepository.findByUserID(userID);
        response.setStatus(HttpStatus.OK.value());
        response.setResult(items);
        return new ResponseEntity<>(
            response,
            HttpStatus.OK
        );
    }

    public List<Item> getItemsByItemTypeID(ObjectId itemTypeID) {
        return itemRepository.findByItemTypeID(itemTypeID);
    }

    public ResponseEntity<ApiResponse<List<ItemWithDetails>>> getItemsOfUserInAnEvent(String userID, Long eventID) {
        ApiResponse<List<ItemWithDetails>> response = new ApiResponse<>();
        List<ItemWithDetails> items = itemRepository.getItemsOfUserInAnEvent(userID, eventID);
        response.setStatus(HttpStatus.OK.value());
        response.setResult(items);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(ObjectId id) {
        itemRepository.deleteById(id);
    }
    public ResponseEntity<ApiResponse<ItemType>> getRandomItem(GetRandomItemTypeDTO getRandomItemTypeDTO) {
        String userID = getRandomItemTypeDTO.getUserID();
        ObjectId gameID = getRandomItemTypeDTO.getGameID();
        Long eventID = getRandomItemTypeDTO.getEventID();

        ApiResponse<ItemType> response = new ApiResponse<>();
        List<ItemType> itemTypes = itemTypeRepository.findAll();
        System.out.println("itemTypes: " + itemTypes.size());
        int randomIdx = (int) (Math.random() * itemTypes.size());
        ItemType randomItem = itemTypes.get(randomIdx);
        Optional<Participant> participant = participantRepository.findParticipantByEventGameAndUser(
                eventID,
                gameID,
                userID
        );

        if (participant.isEmpty()) {
            response.setMessage("Please participating before shake item");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // if user already had item type in that game and event, increase count
        Item userItem = itemRepository.findByEventIDAndGameIDAndItemTypeIDAndUserID(eventID, gameID, randomItem.getItemTypeID(), userID);
        if (userItem != null) {
            userItem.setQuantity(userItem.getQuantity() + 1);
        } else {
            userItem = new Item();
            userItem.setQuantity(1);
            userItem.setUserID(userID); // need to get user id
            userItem.setEventID(eventID);
            userItem.setGameID(gameID);
            userItem.setItemTypeID(randomItem.getItemTypeID());
        }
        itemRepository.save(userItem);
        participant.get().setTurnLeft(participant.get().getTurnLeft() - 1);
        participantRepository.save(participant.get());

        // Send kafka message to report service for tracking user shake game activity


        response.setStatus(HttpStatus.OK.value());
        response.setMessage("You just achieved an item");
        response.setResult(randomItem);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
    public ResponseEntity<ApiResponse<String>> exchangeItemsBetweenUsers(ExchangeItemsRequest exchangeItemsRequest) {
        System.out.println(exchangeItemsRequest.toString());
        Long eventID = exchangeItemsRequest.getEventID();
        ObjectId gameID = exchangeItemsRequest.getGameID();
        ObjectId itemTypeIDA = exchangeItemsRequest.getItemTypeID();
        String userIDA = exchangeItemsRequest.getUserIDA();
        String userIDB = exchangeItemsRequest.getUserIDB();
        Item itemA = itemRepository.findByEventIDAndGameIDAndItemTypeIDAndUserID(eventID, gameID, itemTypeIDA, userIDA);
        Item itemB = itemRepository.findByEventIDAndGameIDAndItemTypeIDAndUserID(eventID, gameID, itemTypeIDA, userIDB);
        System.out.println(itemA);
        System.out.println(itemB);
        if (itemA == null || itemB == null || itemA.getQuantity() < 1 || itemB.getQuantity() < 1) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                            "User does not have this item to exchange", null),
                    HttpStatus.BAD_REQUEST);
        }
        // Update item user A and item user B
        itemA.setQuantity(itemA.getQuantity() - 1);
        itemB.setQuantity(itemB.getQuantity() + 1);
        itemRepository.save(itemA);
        itemRepository.save(itemB);

        // Kafka message


        // Save history
        String currentTime = String.valueOf(System.currentTimeMillis());
        ExchangeHistory history = new ExchangeHistory();
        history.setItemTypeID(itemTypeIDA);
        history.setPresenterID(userIDA);
        history.setReceiverID(userIDB);
        history.setTimeSent(currentTime);
        history.setGameID(gameID);
        history.setEventID(eventID);
        exchangeHistoryRepository.save(history);
        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(),
                        "Exchange successfully", null),
                HttpStatus.OK
        );
    }
}

