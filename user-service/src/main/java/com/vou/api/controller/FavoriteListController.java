package com.vou.api.controller;

import com.vou.api.entity.FavoriteList;
import com.vou.api.service.FavoriteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite-list")
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