package com.vou.api.service;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.CreateEventRequest;
import com.vou.api.entity.PromotionalEvent;
import com.vou.api.mapper.EventMapper;
import com.vou.api.repository.PromotionalEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionalEventService {
    private final PromotionalEventRepository repository;

    private final KafkaProducerService kafkaProducerService;

    public ResponseEntity<ApiResponse<List<PromotionalEvent>>> getEventMatchEventName(String nameOfEvent) {
        if(nameOfEvent.isBlank())
            return getAllEvents();

        List<PromotionalEvent> promotionalEvents = repository.findEventMatchEventName(nameOfEvent);
        ApiResponse<List<PromotionalEvent>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setResult(promotionalEvents);

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    public ResponseEntity<ApiResponse<PromotionalEvent>> createEvent(CreateEventRequest event) {
        PromotionalEvent promotionalEvent = PromotionalEvent.builder()
                .brandID(event.getBrandID())
                .eventBanner(event.getEventBanner())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .nameOfEvent(event.getNameOfEvent())
                .description(event.getDescription())
                .build();
        PromotionalEvent savedEvent = repository.save(promotionalEvent);
        ApiResponse<PromotionalEvent> response = new ApiResponse<>();
        kafkaProducerService.sendCreateEventMessage(EventMapper.convertToEventMessage(savedEvent));
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Event created successfully");
        response.setResult(promotionalEvent);
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    public PromotionalEvent updateEvent(Long eventId, PromotionalEvent eventDetails) {
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

    public void deleteEvent(Long eventId) {
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

    public ResponseEntity<ApiResponse<List<PromotionalEvent>>> searchEventByBrandID(String nameOfEvent, String brandID) {
        List<PromotionalEvent> promotionalEvents = repository.findEventMatchEventNameByBrand(nameOfEvent, brandID);
        ApiResponse<List<PromotionalEvent>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setResult(promotionalEvents);

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    public Optional<PromotionalEvent> getEventById(Long eventId) {
        return repository.findById(eventId);
    }
}
