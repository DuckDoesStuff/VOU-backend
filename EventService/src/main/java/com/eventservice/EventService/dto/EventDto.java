package com.eventservice.EventService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EventDto {
    @NotNull(message = "Brand UUID is required")
    private UUID brandID;

    @NotNull(message = "Event name is required")
    private String nameOfEvent;

    @NotNull(message = "Event description is required")
    private String description;

    @NotNull(message = "Event banner is required")
    private String eventBanner;

    @NotNull(message = "Event start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "Event end time is required")
    private LocalDateTime endTime;
}
