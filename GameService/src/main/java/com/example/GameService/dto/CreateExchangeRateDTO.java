package com.example.GameService.dto;

import com.example.GameService.custom.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class CreateExchangeRateDTO {
    public Long eventID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    public ObjectId gameID;
    @JsonSerialize(using = ObjectIdSerializer.class)
    public ObjectId itemTypeID;
    public Long voucherTypeID;
    public int exchangeRate;
}
