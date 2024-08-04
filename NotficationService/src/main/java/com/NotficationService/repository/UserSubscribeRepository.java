package com.NotficationService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import com.NotficationService.entity.UserSubscribe;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscribeRepository extends MongoRepository<UserSubscribe, String> {
    Optional<UserSubscribe> findByUserID(String userID);
}
