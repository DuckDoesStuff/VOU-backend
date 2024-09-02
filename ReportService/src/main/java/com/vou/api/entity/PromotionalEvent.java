package com.vou.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "promotional_event")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionalEvent {
    @Id
    @Column(name = "eventID")
    private Long eventID;

    @Column(name = "name_of_event", nullable = false)
    private String nameOfEvent;

    @Column(name = "eventBanner")
    private String eventBanner;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "startTime")
    private LocalDateTime startTime;

    @Column(name = "endTime")
    private LocalDateTime endTime;

    @Column(name = "brandID", nullable = false)
    private String brandID;
}
