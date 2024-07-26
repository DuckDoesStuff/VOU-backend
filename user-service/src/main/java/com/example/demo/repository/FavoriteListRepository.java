package com.example.demo.repository;

import com.example.demo.entity.FavoriteList;
import com.example.demo.entity.FavoriteListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteList, FavoriteListId> {
    List<FavoriteList> findByUserID(Integer userID);
}