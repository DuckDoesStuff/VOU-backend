package com.eventservice.EventService.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "voucher_type")
@Builder
@Entity
public class VoucherType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long voucherTypeID;

    @ManyToOne
    @JoinColumn(name = "promotional_event_id")
    private PromotionalEvent promotionalEvent;

    private int totalQuantity;
    private int inStock;
    private LocalDateTime expiryDay;

    private String nameOfVoucher;
    private String picture;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String value;
}
