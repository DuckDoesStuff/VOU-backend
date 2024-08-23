package com.vou.api.entity;

// Participant.java
import com.vou.api.custom.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "participants")
@Getter
@Setter
public class Participant {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private Long eventID;
    private String userID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
    private int turnLeft;

}
