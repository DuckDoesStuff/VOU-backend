package com.vou.api.entity;

// ExchangeRate.java
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exchange_rates")
public class ExchangeRate {
    @Id
    private String id;
    private Long eventID;
    private Long gameID;
    private Long itemTypeID;
    private Long voucherTypeID;
    private double exchangeRate;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    public Long getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(Long itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public Long getVoucherTypeID() {
        return voucherTypeID;
    }

    public void setVoucherTypeID(Long voucherTypeID) {
        this.voucherTypeID = voucherTypeID;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
