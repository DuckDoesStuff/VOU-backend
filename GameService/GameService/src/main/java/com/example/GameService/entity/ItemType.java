package com.example.GameService.entity;

// ItemType.java
import com.example.GameService.custom.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
