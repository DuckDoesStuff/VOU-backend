package com.vou.api.controller;

// ItemController.java
import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ExchangeItemsRequest;
import com.vou.api.dto.GetRandomItemTypeDTO;
import com.vou.api.dto.ItemWithDetails;
import com.vou.api.entity.Item;
import com.vou.api.entity.ItemType;
import com.vou.api.service.ItemService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable ObjectId id) {
        return itemService.getItem(id);
    }

    @GetMapping("/event/{eventID}")
    public List<Item> getItemsByEventID(@PathVariable Long eventID) {
        return itemService.getItemsByEventID(eventID);
    }

    @GetMapping("/game/{gameID}")
    public List<Item> getItemsByGameID(@PathVariable ObjectId gameID) {
        return itemService.getItemsByGameID(gameID);
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<ApiResponse<List<Item>>> getItemsByUserID(@PathVariable String userID) {
        return itemService.getItemsByUserID(userID);
    }
    // Get items of user in an event
    @GetMapping("/user/{userID}/event/{eventID}")
    public ResponseEntity<ApiResponse<List<ItemWithDetails>>> getItemsOfUserInAnEvent(@PathVariable String userID, @PathVariable Long eventID) {
        System.out.println("Get items of a user in an event" + userID + eventID.toString());
        return itemService.getItemsOfUserInAnEvent(userID, eventID);
    }

    // Get items of user in a game
    @GetMapping("/user/{userID}/event/{eventID}/game/{gameID}")
    public ResponseEntity<ApiResponse<List<ItemWithDetails>>> getItemsOfUserInAnEvent(@PathVariable String userID,
                                                                                      @PathVariable Long eventID,
                                                                                      @PathVariable ObjectId gameID) {
        System.out.println("Get items of a user in a game" + userID + eventID.toString());
        return itemService.getItemsOfUserInAGame(userID, gameID, eventID);
    }


    @GetMapping("/itemType/{itemTypeID}")
    public List<Item> getItemsByItemTypeID(@PathVariable ObjectId itemTypeID) {
        return itemService.getItemsByItemTypeID(itemTypeID);
    }

    @PostMapping
    public Item saveItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable ObjectId id) {
        itemService.deleteItem(id);
    }

        @PostMapping("/random_item_type")
    public ResponseEntity<ApiResponse<ItemType>> getRandomItemType(@RequestBody GetRandomItemTypeDTO getRandomItemTypeDTO) {
//        System.out.println(getRandomItemTypeDTO.toString());
        return itemService.getRandomItem(getRandomItemTypeDTO);
    }
    @PostMapping("/exchange")
    public ResponseEntity<ApiResponse<String>> exchangeItemBetweenUsers(@RequestBody ExchangeItemsRequest exchangeItemsRequest)  {
        return itemService.exchangeItemsBetweenUsers(exchangeItemsRequest);
    }
}

