package com.NotficationService.controller;

import com.NotficationService.dto.ApiResponse;
import com.NotficationService.dto.request.PushTopicNotificationRequest;
import com.NotficationService.dto.response.ClientResponse;
import com.NotficationService.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {
    NotificationService notificationService;

    @PostMapping("/sendPushTopicNotification")
    ApiResponse<String> sendPushTopicNotification(@RequestBody @Valid PushTopicNotificationRequest pushNotificationRequest) {
        return ApiResponse.<String>builder()
                .data(notificationService.sendGeneralPushNotification(pushNotificationRequest))
                .build();
    }

    @GetMapping("/getPushNotification")
    ApiResponse<ClientResponse> getPushNotification(@RequestParam(name="userID") String userID) {
        return ApiResponse.<ClientResponse>builder()
                .data(new ClientResponse(notificationService.getPushNotificationOfUser(userID)))
                .build();
    }

//    @PostMapping
//    ApiResponse<String> sendSpecificNotification(@RequestBody TopicNotificationRequest notification) {
//        return ApiResponse.<String>builder()
//                .data(notificationService.sendPushNotification(notification))
//                .build();
//    }
//
//    @KafkaListener(topics = "multipleUserNotification")
//    void listen(String message){
//        log.info("Message received: {}", message);
//    }
}
