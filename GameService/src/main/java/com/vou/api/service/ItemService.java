package com.vou.api.service;

// ItemService.java
import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ExchangeItemsRequest;
import com.vou.api.dto.GetRandomItemTypeDTO;
import com.vou.api.entity.ExchangeHistory;
import com.vou.api.entity.Item;
import com.vou.api.entity.ItemType;
import com.vou.api.entity.Participant;
import com.vou.api.repository.ExchangeHistoryRepository;
import com.vou.api.repository.ItemRepository;
import com.vou.api.repository.ItemTypeRepository;
import com.vou.api.repository.ParticipantRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ExchangeHistoryRepository exchangeHistoryRepository;
    @Autowired
    private ParticipantRepository participantRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItem(String id) {
        return itemRepository.findById(id).orElse(null);
    }

    public List<Item> getItemsByEventID(Long eventID) {
        return itemRepository.findByEventID(eventID);
    }

    public List<Item> getItemsByGameID(ObjectId gameID) {
        return itemRepository.findByGameID(gameID);
    }

    public ResponseEntity<ApiResponse<List<Item>>> getItemsByUserID(Long userID) {
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

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }
    public ResponseEntity<ApiResponse<ItemType>> getRandomItem(GetRandomItemTypeDTO getRandomItemTypeDTO) {
        Long userID = getRandomItemTypeDTO.getUserID();
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
        // Decrement user turnLeft by 1
        if (participant.isPresent()) {
            participant.get().setTurnLeft(participant.get().getTurnLeft() - 1);
            participantRepository.save(participant.get());
        }

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("You just achieved an item");
        response.setResult(randomItem);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
    public ResponseEntity<ApiResponse<String>> exchangeItemsBetweenUsers(ExchangeItemsRequest exchangeItemsRequest) {
        Long eventID = exchangeItemsRequest.getItemA().getEventID();
        ObjectId gameID = exchangeItemsRequest.getItemA().getGameID();
        ObjectId itemTypeIDA = exchangeItemsRequest.getItemA().getItemTypeID();
        Long userIDA = exchangeItemsRequest.getItemA().getUserID();
        Long userIDB = exchangeItemsRequest.getItemB().getUserID();
        Item itemA = itemRepository.findByEventIDAndGameIDAndItemTypeIDAndUserID(eventID, gameID, itemTypeIDA, userIDA);
        Item itemB = itemRepository.findByEventIDAndGameIDAndItemTypeIDAndUserID(eventID, gameID, itemTypeIDA, userIDB);

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

