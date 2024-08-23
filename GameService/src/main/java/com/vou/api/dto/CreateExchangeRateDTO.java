package com.vou.api.dto;

import com.vou.api.custom.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vou.api.custom.ObjectIdSerializer;
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
