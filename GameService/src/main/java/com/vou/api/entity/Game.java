package com.vou.api.entity;

import com.vou.api.custom.ObjectIdSerializer;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private Integer defaultFreeTurn;

    private List<Question> questions;

    private String quizState; // "PREPARING" | "READY" | "STREAMING" | "ENDED"

    @Data
    public static class Question {
        private String question;
        private List<String> answers;
        private Integer correctAnswer;
        private String video;
        private String videoStatus;
    }
}
