package com.vou.api.mapper;

import com.vou.api.dto.EventMessage;
import com.vou.api.entity.PromotionalEvent;

public class EventMapper {
    public static EventMessage convertToEventMessage(PromotionalEvent promotionalEvent) {
        return EventMessage.builder()
                .eventID(promotionalEvent.getEventID())
                .brandID(promotionalEvent.getBrandID())
                .nameOfEvent(promotionalEvent.getNameOfEvent())
                .eventBanner(promotionalEvent.getEventBanner())
                .description(promotionalEvent.getDescription())
                .startTime(promotionalEvent.getStartTime())
                .endTime(promotionalEvent.getEndTime())
                .build();
    }
}
