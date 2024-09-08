package com.vou.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vou.api.custom.ObjectIdSerializer;
import com.vou.api.entity.ItemType;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ItemWithDetails {
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private Long eventID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId itemTypeID;
    private String userID;
    private int quantity;
    private ItemType itemTypeDetails;
}
