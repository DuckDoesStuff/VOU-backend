package com.vou.api.entity;

// Item.java
import com.vou.api.custom.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "items")
@Data
public class Item {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private Long eventID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId itemTypeID;
    private String userID;
    private int quantity;
}
