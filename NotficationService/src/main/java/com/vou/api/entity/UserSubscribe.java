package com.vou.api.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("UserSubscribe")
@Getter
@Setter
@Data
public class UserSubscribe {
    @Id
    String userID;
    List<SubscribeItem> subscribeList;

    public boolean checkIfInSubcribeList(SubscribeItem subscribeItem) {
        if (subscribeList.contains(subscribeItem)) {
            return true;
        }
        return false;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeItem {
        String topic;
        String typeSubscribe;
        LocalDateTime dateSubscribe;
        LocalDateTime dateUnsubscribe;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SubscribeItem that = (SubscribeItem) o;
            if (this.topic.equals(that.topic)) {
                return true;
            }
            return false;
        }
    }
}

