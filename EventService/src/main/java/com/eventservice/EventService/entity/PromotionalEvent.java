package com.eventservice.EventService.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "promotional_event")
public class PromotionalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private UUID eventID;

    @Column(nullable = false)
    private UUID brandID;

    @Column(nullable = false)
    private String nameOfEvent;

    @Column(nullable = false)
    private String eventBanner;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "event")
    private Set<VoucherType> vouchers;
}
