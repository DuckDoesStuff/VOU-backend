package com.example.GameService.dto;

import com.vou.api.custom.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vou.api.custom.ObjectIdSerializer;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class ExchangeRateDTO {
    public Long eventID;
    public Long voucherTypeID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    public ObjectId itemTypeID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    public ObjectId gameID;
    public int exchangeRate;
}
