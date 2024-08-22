package com.example.GameService.entity;

import com.example.GameService.custom.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "games")
@Data
public class Game {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
    private Long eventID;
    private String nameOfGame;
    private String picture;
    private String instruction;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type; //QUIZ or SHAKE

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer defaultFreeTurn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Question> questions;

    @Data
    public static class Question {
        private String question;
        private List<String> answers;
        private Integer correctAnswer;
        private String video;
        private String videoStatus;
    }
}
