package com.vou.api.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ExchangeForVoucherDTO {
    public String userID;
    public Long eventID;
    public Long voucherTypeID;
    public ObjectId itemTypeID;
    public ObjectId gameID;
    public int exchangeRate;
}
