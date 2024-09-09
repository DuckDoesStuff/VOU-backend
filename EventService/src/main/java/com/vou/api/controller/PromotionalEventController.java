package com.vou.api.controller;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.CreateEventRequest;
import com.vou.api.entity.PromotionalEvent;
import com.vou.api.service.PromotionalEventService;
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

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PromotionalEvent>>> getEventMatchEventName(@RequestParam("nameOfEvent") String nameOfEvent){
        return promotionalEventService.getEventMatchEventName(nameOfEvent);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<PromotionalEvent>> createEvent(@RequestBody CreateEventRequest event) {
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
