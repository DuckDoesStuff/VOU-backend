package com.example.GameService.entity;

// ExchangeHistory.java
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exchange_history")
@Data
public class ExchangeHistory {
    @Id
    private String id;
    private Long presenterID;
    private Long receiverID;
    private String timeSent;
    private Long itemTypeID;
    private Long eventID;
    private Long gameID;
}

