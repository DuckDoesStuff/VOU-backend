package com.example.GameService.service;

// ItemTypeService.java
import com.example.GameService.entity.ItemType;
import com.example.GameService.repository.ItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemTypeService {
    @Autowired
    private ItemTypeRepository itemTypeRepository;

    public List<ItemType> getAllItemTypes() {
        return itemTypeRepository.findAll();
    }

    public ItemType getItemType(String id) {
        return itemTypeRepository.findById(id).orElse(null);
    }

    public List<ItemType> getItemTypesByEventID(Long eventID) {
        return itemTypeRepository.findByEventID(eventID);
    }

    public List<ItemType> getItemTypesByGameID(Long gameID) {
        return itemTypeRepository.findByGameID(gameID);
    }

    public List<ItemType> getItemTypesByType(String type) {
        return itemTypeRepository.findByType(type);
    }

    public ItemType saveItemType(ItemType itemType) {
        return itemTypeRepository.save(itemType);
    }

    public void deleteItemType(String id) {
        itemTypeRepository.deleteById(id);
    }
}
