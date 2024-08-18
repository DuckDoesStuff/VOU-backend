package com.vou.user_service.service;

import com.vou.user_service.entity.FavoriteList;
import com.vou.user_service.repository.FavoriteListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteListService {
    @Autowired
    private FavoriteListRepository favoriteListRepository;

    public FavoriteList addFavoriteList(FavoriteList favoriteList) {
        return favoriteListRepository.save(favoriteList);
    }

    public List<FavoriteList> getFavoriteListByUserID(Integer userID) {
        return favoriteListRepository.findByUserID(userID);
    }
}
