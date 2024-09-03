package com.vou.api.repository;


import com.vou.api.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByTopicIn(List<String> topics);
    List<Notification> findByReceiverContaining(String userId);
}
