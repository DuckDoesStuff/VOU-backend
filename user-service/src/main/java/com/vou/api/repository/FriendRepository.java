package com.vou.api.repository;

import com.vou.api.entity.Friend;
import com.vou.api.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFriend(UserProfile friend);
    List<Friend> findByUser(UserProfile user);
}
