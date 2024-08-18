package com.vou.user_service.repository;

import com.vou.user_service.entity.Friend;
import com.vou.user_service.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFriend(UserProfile friend);
    List<Friend> findByUser(UserProfile user);
}
