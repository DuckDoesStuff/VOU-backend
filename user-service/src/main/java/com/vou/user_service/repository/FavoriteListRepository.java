package com.vou.user_service.repository;

import com.vou.user_service.entity.FavoriteList;
import com.vou.user_service.entity.FavoriteListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteList, FavoriteListId> {
    List<FavoriteList> findByUserID(Integer userID);
}