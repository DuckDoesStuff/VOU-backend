package com.example.GameService.service;

// ItemService.java
import com.example.GameService.entity.Item;
import com.example.GameService.entity.ItemType;
import com.example.GameService.repository.ItemRepository;
import com.example.GameService.repository.ItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;

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
    public ItemType getRandomItem(Long eventID, Long gameID) {
        List<ItemType> itemTypes = itemTypeRepository.findAll();
        int randomIdx = (int) (Math.random() * itemTypes.size());
        ItemType randomItem = itemTypes.get(randomIdx);
        // if user already had item type in that game and event, increase count
        Item userItem = itemRepository.findByEventIDAndGameID(eventID, gameID);
        if (userItem != null) {
            userItem.setQuantity(userItem.getQuantity() + 1);
        } else {
            userItem = new Item();
            userItem.setQuantity(1);
            userItem.setUserID((long) 4); // need to get user id
            userItem.setEventID(eventID);
            userItem.setGameID(gameID);
            userItem.setItemTypeID(randomItem.getItemTypeID());
        }
        itemRepository.save(userItem);
        return randomItem;
    }
}

