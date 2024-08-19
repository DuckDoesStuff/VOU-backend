package com.eventservice.EventService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor // This adds a public no-argument constructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "promotional_event")
public class PromotionalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long eventID;

    @Column(nullable = false)
    private UUID brandID;

    @Column(nullable = false)
    private String nameOfEvent;

    @Column(nullable = false)
    private String eventBanner;

    @Column(nullable = false, length = 2500, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "promotionalEvent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VoucherType> vouchers;
}
