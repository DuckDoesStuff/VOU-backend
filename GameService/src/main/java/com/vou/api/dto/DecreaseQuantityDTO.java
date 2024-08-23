package com.vou.api.dto;

import lombok.Data;

@Data
public class DecreaseQuantityDTO {
    Long voucherTypeID;
    int totalDecreased;
}
