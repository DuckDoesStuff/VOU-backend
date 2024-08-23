package com.vou.api.entity;

// ItemType.java
import com.vou.api.custom.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "item_types")
@Data
public class ItemType {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId itemTypeID;
    private Long eventID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
    private String nameOfItem;
    private String picture;
    private String type;
}
