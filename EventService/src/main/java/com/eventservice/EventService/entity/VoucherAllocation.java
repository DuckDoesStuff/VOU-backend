package com.eventservice.EventService.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "voucher_allocation")
@Entity
public class VoucherAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long eventID;
    public long gameID;
    public long voucherTypeID;
    public int totalQuantity;
    public int inStock;
    public LocalDateTime expiryDay;
}
