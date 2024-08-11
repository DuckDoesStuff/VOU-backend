package com.example.GameService.entity;

import com.example.GameService.custom.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "games")
@Data
public class Game {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
    private Long eventID;
    private String nameOfGame;
    private String picture;
    private String type;
    private String instruction;
    private Integer defaultFreeTurn;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
