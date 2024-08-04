package com.NotficationService.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("UserSubscribe")
@Getter
@Setter
public class UserSubscribe {
    @Id
    String userID;
    List<SubscribeItem> subscribeList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeItem {
        String topic;
        String typeSubscribe;
    }
}

