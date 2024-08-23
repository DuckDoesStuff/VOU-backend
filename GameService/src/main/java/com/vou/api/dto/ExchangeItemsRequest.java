package com.vou.api.dto;

import com.vou.api.entity.Item;
import lombok.Data;

@Data
public class ExchangeItemsRequest {
    public Item itemA;
    public Item itemB;
}
