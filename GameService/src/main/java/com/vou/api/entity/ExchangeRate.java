package com.vou.api.entity;

// ExchangeRate.java
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exchange_rates")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    @Id
    private ObjectId id;
    private Long eventID;
    private ObjectId gameID;
    private ObjectId itemTypeID;
    private Long voucherTypeID;
    private int exchangeRate;
}
