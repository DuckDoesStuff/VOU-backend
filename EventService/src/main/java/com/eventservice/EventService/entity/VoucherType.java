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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Long voucherTypeID;

    @ManyToOne
    @JoinColumn
    public PromotionalEvent event;

    public int totalQuantity;
    public int inStock;
    public LocalDateTime expiryDay;

    public String nameOfVoucher;
    public String picture;
    @Column(columnDefinition = "TEXT")
    public String description;
    public String value;
}
