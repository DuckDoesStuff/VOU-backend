package com.eventservice.EventService.controller;

import com.eventservice.EventService.entity.PromotionalEvent;
import com.eventservice.EventService.service.PromotionalEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class PromotionalEventController {

    @Autowired
    private PromotionalEventService service;

    @PostMapping()
    public ResponseEntity<PromotionalEvent> createEvent(@RequestBody PromotionalEvent event) {
        PromotionalEvent createdEvent = service.createEvent(event);
        return ResponseEntity.ok(createdEvent);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PromotionalEvent> updateEvent(@PathVariable Long id, @RequestBody PromotionalEvent eventDetails) {
        PromotionalEvent updatedEvent = service.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PromotionalEvent>> getAllEvents() {
        List<PromotionalEvent> events = service.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionalEvent> getEventById(@PathVariable Long id) {
        Optional<PromotionalEvent> event = service.getEventById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
