package com.vou.api.dto;

import lombok.Data;

@Data
public class DecreaseVoucherDTO {
    public Long voucherTypeID;
    public int totalDecreased;
}
