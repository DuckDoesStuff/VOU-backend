package com.vou.api.repository;

import com.vou.api.entity.FavoriteList;
import com.vou.api.entity.FavoriteListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteList, FavoriteListId> {
    List<FavoriteList> findByUserID(Integer userID);
}