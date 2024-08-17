package com.eventservice.EventService.service;

import com.eventservice.EventService.dto.ApiResponse;
import com.eventservice.EventService.dto.EventDto;
import com.eventservice.EventService.repository.PromotionalEventRepository;
import com.eventservice.EventService.entity.PromotionalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PromotionalEventService {
    @Autowired
    private PromotionalEventRepository repository;

    public ResponseEntity<ApiResponse<PromotionalEvent>> createEvent(PromotionalEvent event) {
        PromotionalEvent promotionalEvent = repository.save(event);
        ApiResponse<PromotionalEvent> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Event created successfully");
        response.setResult(promotionalEvent);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public PromotionalEvent updateEvent(UUID eventId, PromotionalEvent eventDetails) {
        Optional<PromotionalEvent> optionalEvent = repository.findById(eventId);
        if (optionalEvent.isPresent()) {
            PromotionalEvent event = optionalEvent.get();
            event.setNameOfEvent(eventDetails.getNameOfEvent());
//            event.setNumberOfVoucher(eventDetails.getNumberOfVoucher());
            event.setStartTime(eventDetails.getStartTime());
            event.setEndTime(eventDetails.getEndTime());
            event.setBrandID(eventDetails.getBrandID());
            return repository.save(event);
        } else {
            throw new RuntimeException("Event not found with id " + eventId);
        }
    }

    public void deleteEvent(UUID eventId) {
        repository.deleteById(eventId);
    }

    public ResponseEntity<ApiResponse<List<PromotionalEvent>>> getAllEvents() {
        List<PromotionalEvent> promotionalEvent = repository.findAll();
        ApiResponse<List<PromotionalEvent>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get events");
        response.setResult(promotionalEvent);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    // Subject to change
    public ResponseEntity<ApiResponse<List<Long>>> getEventsByBrandID(UUID brandID) {
        List<Long> eventIDs = repository.getEventsIDByBrandID(brandID);
        ApiResponse<List<Long>> response = new ApiResponse<>();
        if (eventIDs == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid brandID or unable to fetch eventIDs");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (eventIDs.isEmpty()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("No eventIDs found for the given brandID");
            response.setResult(eventIDs);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get eventIDs");
        response.setResult(eventIDs);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public ResponseEntity<ApiResponse<List<PromotionalEvent>>> getPromotionalEventsByBrandID(UUID brandID) {
        List<PromotionalEvent> eventIDs = repository.findByBrandID(brandID);
        ApiResponse<List<PromotionalEvent>> response = new ApiResponse<>();
        if (eventIDs == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid brandID or unable to fetch eventIDs");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (eventIDs.isEmpty()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("No eventIDs found for the given brandID");
            response.setResult(eventIDs);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get eventIDs");
        response.setResult(eventIDs);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public Optional<PromotionalEvent> getEventById(UUID eventId) {
        return repository.findById(eventId);
    }
}
