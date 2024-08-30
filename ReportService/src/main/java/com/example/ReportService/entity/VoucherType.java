package com.example.ReportService.entity;

import jakarta.persistence.Entity;
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
    public Long voucherTypeID;
    public Long eventID;
    public String gameID;
    public int totalQuantity;
    public int inStock;
    public LocalDateTime expiryDate;
    public String picture;
}
