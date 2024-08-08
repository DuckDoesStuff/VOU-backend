package com.eventservice.EventService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
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
