package com.example.GameService.entity;

// Participant.java
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "participants")
public class Participant {
    @Id
    private String id;
    private Long eventID;
    private Long userID;
    private Long gameID;
    private int turnLeft;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    public int getTurnLeft() {
        return turnLeft;
    }

    public void setTurnLeft(int turnLeft) {
        this.turnLeft = turnLeft;
    }
}
