package com.vou.api.repository;

import com.vou.api.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findUserProfileByUsername(String username);
    Optional<UserProfile> findUserProfileByPhone(String phone);
    Optional<UserProfile> findUserProfileByEmail(String email);

    @Query("SELECT u FROM UserProfile u WHERE u.username LIKE :username%")
    List<UserProfile> searchByUsername(@Param("username") String username);
}
