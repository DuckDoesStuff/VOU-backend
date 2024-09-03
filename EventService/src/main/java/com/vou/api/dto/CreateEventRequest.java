package com.vou.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    private UUID brandID;
    private String nameOfEvent;
    private String eventBanner;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
