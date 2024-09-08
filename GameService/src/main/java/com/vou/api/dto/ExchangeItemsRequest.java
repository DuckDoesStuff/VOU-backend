package com.vou.api.dto;

import com.vou.api.entity.Item;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ExchangeItemsRequest {
    private String userIDA;
    private String userIDB;
    private Long eventID;
    private ObjectId gameID;
    private ObjectId itemTypeID;
}
