package com.eventservice.EventService.dto;

import lombok.Data;

@Data
public class DecreaseVoucherDTO {
    public Long voucherTypeID;
    public int totalDecreased;
}
