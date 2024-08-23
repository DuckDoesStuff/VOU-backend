package com.example.GameService.entity;

// ExchangeHistory.java
import com.example.GameService.custom.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "exchange_history")
@Data
public class ExchangeHistory {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private String id;
    private String presenterID;
    private String receiverID;
    private String timeSent;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId itemTypeID;
    private Long eventID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
}

