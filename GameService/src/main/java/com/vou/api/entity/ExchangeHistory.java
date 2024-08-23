package com.vou.api.entity;

// ExchangeHistory.java
import com.vou.api.custom.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exchange_history")
@Data
public class ExchangeHistory {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private String id;
    private Long presenterID;
    private Long receiverID;
    private String timeSent;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId itemTypeID;
    private Long eventID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
}

