package com.example.demo.controller;

import com.example.demo.entity.FavoriteList;
import com.example.demo.service.FavoriteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite-list")
public class FavoriteListController {

    @Autowired
    private FavoriteListService favoriteListService;

    @PostMapping
    public FavoriteList addFavoriteList(@RequestBody FavoriteList favoriteList) {
        return favoriteListService.addFavoriteList(favoriteList);
    }

    @GetMapping("/user/{userId}")
    public List<FavoriteList> getFavoriteListByUserID(@PathVariable Integer userId) {
        return favoriteListService.getFavoriteListByUserID(userId);
    }
}