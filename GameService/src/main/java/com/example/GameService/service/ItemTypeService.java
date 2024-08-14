package com.example.GameService.service;

// ItemTypeService.java
import com.example.GameService.dto.ApiResponse;
import com.example.GameService.entity.Item;
import com.example.GameService.entity.ItemType;
import com.example.GameService.repository.ItemTypeRepository;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemTypeService {
    @Autowired
    private ItemTypeRepository itemTypeRepository;

    public ResponseEntity<ApiResponse<List<ItemType>>> getAllItemTypes() {
        ApiResponse<List<ItemType>> response = new ApiResponse<>();
        List<ItemType> itemTypes = itemTypeRepository.findAll();
        response.setStatus(HttpStatus.OK.value());
        response.setResult(itemTypes);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public ItemType getItemType(ObjectId id) {
        return itemTypeRepository.findById(id).orElse(null);
    }

    public List<ItemType> getItemTypesByEventID(Long eventID) {
        return itemTypeRepository.findByEventID(eventID);
    }

    public ResponseEntity<ApiResponse<List<ItemType>>> getItemTypesByGameID(ObjectId gameID) {
        List<ItemType> itemTypes =  itemTypeRepository.findByGameID(gameID);
        ApiResponse<List<ItemType>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setResult(itemTypes);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public List<ItemType> getItemTypesByType(String type) {
        return itemTypeRepository.findByType(type);
    }

    public ResponseEntity<ApiResponse<ItemType>> saveItemType(ItemType itemType) {
        ItemType newItemType =  itemTypeRepository.save(itemType);
        ApiResponse<ItemType> response = new ApiResponse<>();
        response.setResult(newItemType);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Item Type created successfully");
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public ResponseEntity<ApiResponse<String>> deleteItemType(ObjectId id) {
        itemTypeRepository.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Item type deleted successfully");
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
}
