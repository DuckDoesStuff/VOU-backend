package com.example.GameService.controller;

// ItemController.java
import com.example.GameService.dto.ApiResponse;
import com.example.GameService.dto.ExchangeItemsRequest;
import com.example.GameService.dto.GetRandomItemTypeDTO;
import com.example.GameService.entity.Item;
import com.example.GameService.entity.ItemType;
import com.example.GameService.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Item getItem(@PathVariable String id) {
        return itemService.getItem(id);
    }

    @GetMapping("/event/{eventID}")
    public List<Item> getItemsByEventID(@PathVariable Long eventID) {
        return itemService.getItemsByEventID(eventID);
    }

    @GetMapping("/game/{gameID}")
    public List<Item> getItemsByGameID(@PathVariable Long gameID) {
        return itemService.getItemsByGameID(gameID);
    }

    @GetMapping("/user/{userID}")
    public List<Item> getItemsByUserID(@PathVariable Long userID) {
        return itemService.getItemsByUserID(userID);
    }

    @GetMapping("/itemType/{itemTypeID}")
    public List<Item> getItemsByItemTypeID(@PathVariable Long itemTypeID) {
        return itemService.getItemsByItemTypeID(itemTypeID);
    }

    @PostMapping
    public Item saveItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
    }

    @PostMapping("/random_item_type")
    public ItemType getRandomItemType(@RequestBody GetRandomItemTypeDTO getRandomItemTypeDTO) {
        return itemService.getRandomItem(getRandomItemTypeDTO);
    }
    @PostMapping("/exchange")
    public ResponseEntity<ApiResponse<String>> exchangeItemBetweenUsers(@RequestBody ExchangeItemsRequest exchangeItemsRequest)  {
        return itemService.exchangeItemsBetweenUsers(exchangeItemsRequest);
    }
}

