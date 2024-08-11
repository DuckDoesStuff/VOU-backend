package com.eventservice.EventService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "voucher_type")
@Entity
public class VoucherType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long eventID;
    public long voucherTypeID;
    public String picture;
    public String value;
    public String description;
}
