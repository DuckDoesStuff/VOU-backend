package com.example.GameService.service;

// ItemService.java
import com.example.GameService.entity.Item;
import com.example.GameService.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItem(String id) {
        return itemRepository.findById(id).orElse(null);
    }

    public List<Item> getItemsByEventID(Long eventID) {
        return itemRepository.findByEventID(eventID);
    }

    public List<Item> getItemsByGameID(Long gameID) {
        return itemRepository.findByGameID(gameID);
    }

    public List<Item> getItemsByUserID(Long userID) {
        return itemRepository.findByUserID(userID);
    }

    public List<Item> getItemsByItemTypeID(Long itemTypeID) {
        return itemRepository.findByItemTypeID(itemTypeID);
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }
}

