package com.example.GameService.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "games")
@Data
public class Game {
    @Id
    private String eventID;
    private String gameID;
    private String nameOfGame;
    private String picture;
    private String type;
    private String instruction;
    private String defaultFreeTurn;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
