package com.eventservice.EventService.controller;

import com.eventservice.EventService.dto.ApiResponse;
import com.eventservice.EventService.entity.PromotionalEvent;
import com.eventservice.EventService.service.PromotionalEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/event")
public class PromotionalEventController {

    @Autowired
    private PromotionalEventService promotionalEventService;

    @PostMapping()
    public ResponseEntity<ApiResponse<PromotionalEvent>> createEvent(@RequestBody PromotionalEvent event) {
       return promotionalEventService.createEvent(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionalEvent> updateEvent(@PathVariable Long id, @RequestBody PromotionalEvent eventDetails) {
        PromotionalEvent updatedEvent = promotionalEventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        promotionalEventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PromotionalEvent>>> getAllEvents() {
        return promotionalEventService.getAllEvents();
    }
//    @GetMapping("/brand/{brandID}")
//    public ResponseEntity<ApiResponse<List<Long>>> getAllEvents(@PathVariable Long brandID) {
//        return promotionalEventService.getEventsByBrandID(brandID);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionalEvent> getEventById(@PathVariable Long id) {
        Optional<PromotionalEvent> event = promotionalEventService.getEventById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/brand/{brandID}")
    public ResponseEntity<ApiResponse<List<PromotionalEvent>>> getPromotionalEventsByBrandID(@PathVariable UUID brandID) {
        return promotionalEventService.getPromotionalEventsByBrandID(brandID);
    }

}
