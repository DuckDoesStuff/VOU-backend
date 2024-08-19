package com.eventservice.EventService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "voucher_type")
@Builder
@Entity
public class VoucherType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long voucherTypeID;

    @ManyToOne(fetch = FetchType.LAZY)
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
