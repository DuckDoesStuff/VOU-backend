package com.example.GameService.dto;

import com.example.GameService.entity.Item;
import lombok.Data;

@Data
public class ExchangeItemsRequest {
    public Item itemA;
    public Item itemB;
}
