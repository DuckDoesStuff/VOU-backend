package com.example.GameService.entity;

// GameHistory.java
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Locale;

@Document(collection = "game_history")
@Data
public class GameHistory {
    @Id
    private String id;
    private Long eventID;
    private Long userID;
    private Long gameID;
    private LocalDateTime joinTime;
    private LocalDateTime leftTime;

}
