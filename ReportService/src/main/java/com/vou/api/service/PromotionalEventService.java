package com.vou.api.service;

import com.vou.api.dto.EventMessage;
import com.vou.api.entity.PromotionalEvent;
import com.vou.api.repository.PromotionEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromotionalEventService {
    private final PromotionEventRepository promotionEventRepository;
    @KafkaListener(topics = "create_event")
    public void listenToCreateEvent(EventMessage message) {
        PromotionalEvent promotionalEvent = PromotionalEvent.builder()
                .eventID(message.getEventID())
                .brandID(message.getBrandID().toString())
                .nameOfEvent(message.getNameOfEvent())
                .eventBanner(message.getEventBanner())
                .description(message.getDescription())
                .startTime(message.getStartTime())
                .endTime(message.getEndTime())
                .build();
        promotionEventRepository.save(promotionalEvent);
    }
}
