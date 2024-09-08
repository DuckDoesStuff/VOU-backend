package com.vou.api.entity;

// GameHistory.java
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Document(collection = "game_history")
@Data
public class GameHistory {
    @Id
    private String id;
    private String eventID;
    private String gameID;
    private List<UserInfo> users;


    @Data
    @Builder
    public static class UserInfo {
        private String userID;
        private LocalDateTime joinTime;
        private LocalDateTime leftTime;
    }
}
