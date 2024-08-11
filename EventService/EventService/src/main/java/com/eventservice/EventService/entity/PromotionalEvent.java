package com.eventservice.EventService.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "promotional_event")
public class PromotionalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventID;

    private String nameOfEvent;
    private int numberOfVoucher;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long brandID;
    private long gameID;
}
