package com.eventservice.EventService.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "voucher_type")
@Entity
public class VoucherType {
    @Id
    @Column
    public UUID voucherTypeID;

    @ManyToOne
    @JoinColumn
    public PromotionalEvent event;
    public long gameID;

    public int totalQuantity;
    public int inStock;
    public LocalDateTime expiryDay;

    public String picture;
    public String description;
    public String value;
}
