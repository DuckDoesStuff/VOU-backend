package com.eventservice.EventService.service;

import com.eventservice.EventService.repository.PromotionalEventRepository;
import com.eventservice.EventService.entity.PromotionalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionalEventService {
    @Autowired
    private PromotionalEventRepository repository;

    public PromotionalEvent createEvent(PromotionalEvent event) {
        return repository.save(event);
    }
    public PromotionalEvent updateEvent(Long eventId, PromotionalEvent eventDetails) {
        Optional<PromotionalEvent> optionalEvent = repository.findById(eventId);
        if (optionalEvent.isPresent()) {
            PromotionalEvent event = optionalEvent.get();
            event.setNameOfEvent(eventDetails.getNameOfEvent());
            event.setNumberOfVoucher(eventDetails.getNumberOfVoucher());
            event.setStartTime(eventDetails.getStartTime());
            event.setEndTime(eventDetails.getEndTime());
            event.setBrandID(eventDetails.getBrandID());
            event.setGameID(eventDetails.getGameID());
            return repository.save(event);
        } else {
            throw new RuntimeException("Event not found with id " + eventId);
        }
    }
    public void deleteEvent(Long eventId) {
        repository.deleteById(eventId);
    }

    public List<PromotionalEvent> getAllEvents() {
        return repository.findAll();
    }

    public Optional<PromotionalEvent> getEventById(Long eventId) {
        return repository.findById(eventId);
    }
}
