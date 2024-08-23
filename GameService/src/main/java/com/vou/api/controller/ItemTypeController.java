package com.vou.api.controller;

// ItemTypeController.java
import com.vou.api.dto.ApiResponse;
import com.vou.api.entity.ItemType;
import com.vou.api.service.ItemTypeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item_types")
public class ItemTypeController {
    @Autowired
    private ItemTypeService itemTypeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemType>>> getAllItemTypes() {
        return itemTypeService.getAllItemTypes();

    }

    @GetMapping("/{id}")
    public ItemType getItemType(@PathVariable ObjectId id) {
        return itemTypeService.getItemType(id);
    }

    @GetMapping("/event/{eventID}")
    public List<ItemType> getItemTypesByEventID(@PathVariable Long eventID) {
        return itemTypeService.getItemTypesByEventID(eventID);
    }

    @GetMapping("/game/{gameID}")
    public ResponseEntity<ApiResponse<List<ItemType>>> getItemTypesByGameID(@PathVariable ObjectId gameID) {
        return itemTypeService.getItemTypesByGameID(gameID);
    }


    @GetMapping("/{type}")
    public List<ItemType> getItemTypesByType(@PathVariable String type) {
        return itemTypeService.getItemTypesByType(type);
    }

    @PostMapping("/item_type")
    public ResponseEntity<ApiResponse<ItemType>> saveItemType(@RequestBody ItemType itemType) {
        return itemTypeService.saveItemType(itemType);
    }

    @DeleteMapping("/item_type/{id}")
    public ResponseEntity<ApiResponse<String>> deleteItemType(@PathVariable ObjectId id) {
        return itemTypeService.deleteItemType(id);
    }
}

