package com.vou.api.dto;

// CombinedResult.java
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vou.api.custom.ObjectIdSerializer;
import com.vou.api.entity.ItemType;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class CombinedResult {
    private Long eventID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId gameID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId itemTypeID;
    private Long voucherTypeID;
    private int exchangeRate;
    private ItemType itemTypeDetails;
}

