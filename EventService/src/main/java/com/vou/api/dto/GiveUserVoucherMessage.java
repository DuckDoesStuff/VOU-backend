package com.vou.api.dto;

import lombok.Data;

@Data
public class GiveUserVoucherMessage {
    private String userID;
    private Long voucherTypeID;
}
