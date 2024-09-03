package com.vou.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage {
    private Long eventID;
    private UUID brandID;
    private String nameOfEvent;
    private String description;
    private String eventBanner;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

