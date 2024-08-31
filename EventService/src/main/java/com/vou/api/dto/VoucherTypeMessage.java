package com.vou.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherTypeMessage{
    private Long voucherTypeID;
    private Long eventID;
    private String gameID;
    private int totalQuantity;
    private int inStock;
    private LocalDateTime expiryDate;
}