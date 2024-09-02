package com.vou.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "voucher_type")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_type_id")
    private Long voucherTypeID;

    @Column(name = "event_id")
    private Long eventID;

    @Column(name = "game_id")
    private String gameID;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "in_stock")
    private int inStock;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "picture")
    private String picture;

    @Column(name = "brand_id")
    private String brandID;

    @Column(name = "value")
    private Double value;
}
