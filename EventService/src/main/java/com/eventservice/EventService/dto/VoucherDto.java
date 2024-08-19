package com.eventservice.EventService.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoucherDto {
    @NotNull(message = "EventID is required")
    public Long eventID;

    @NotNull(message = "Total quantity is required")
    public int totalQuantity;
    @NotNull(message = "Expiry date is required")
    public LocalDateTime expiryDate;

    @NotNull(message = "Voucher name is required")
    public String nameOfVoucher;
    public String picture;
    public String description;
    @NotNull(message = "Voucher value is required")
    public String value;
}
