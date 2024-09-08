package com.vou.api.controller;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.request.InfoForStream;
import com.vou.api.dto.request.PushTopicNotificationRequest;
import com.vou.api.dto.request.SubscribeTopicRequest;
import com.vou.api.dto.response.UserNotificationReponse;
import com.vou.api.dto.response.UserSubscribeResponse;
import com.vou.api.mapper.PushNotificationRequestMapper;
import com.vou.api.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("/notification")
public class NotificationController {
    final NotificationService notificationService;
    final PushNotificationRequestMapper pushNotificationRequestMapper;

    @PostMapping("/sendPushTopicNotification")
    ApiResponse<String> sendPushTopicNotification(@RequestBody @Valid PushTopicNotificationRequest pushNotificationRequest) {
        return ApiResponse.<String>builder()
                .result(notificationService.sendGeneralPushNotification(pushNotificationRequest))
                .build();
    }
    @PostMapping("/sendSpecificMessage")
    ApiResponse<String> sendSpecificMessage(@RequestBody @Valid PushTopicNotificationRequest pushNotificationRequest) {
//        log.info("InfoForStream: {}", message);
        log.info(pushNotificationRequest.toString());
        return ApiResponse.<String>builder()
                .result( notificationService.sendSpecificMessage(pushNotificationRequest))
                .build();
    }

    @KafkaListener(topics = "startStream")
    public void listenStartStreamRequest(InfoForStream message) {
//        log.info("InfoForStream: {}", message);
        PushTopicNotificationRequest pushNotificationRequest = pushNotificationRequestMapper.infoForStreamToPNR(message);
        notificationService.sendGeneralPushNotification(pushNotificationRequest);
    }
    @KafkaListener(topics = "sendSpecificMessage")
    public void listenSendSpecificMessage(PushTopicNotificationRequest pushNotificationRequest) {
//        log.info("InfoForStream: {}", message);
        notificationService.sendSpecificMessage(pushNotificationRequest);
    }


    @GetMapping("/getNotification/{userID}")
    ApiResponse<UserNotificationReponse> getPushNotification(@PathVariable("userID") String userID) {
        log.info("hello");
        System.out.println("hjsnjasndj");
        return ApiResponse.<UserNotificationReponse>builder()
                .result(new UserNotificationReponse(notificationService.getPushNotificationOfUser(userID)))
                .build();
    }
//    @GetMapping("/getPushTopic1")
//    ApiResponse<String> getPushTopic1(@RequestParam("userID")String userID, @RequestParam("user")String user) {
//        System.out.print(userID);
//        return ApiResponse.<String>builder()
//                .result(userID+user)
//                .build();
//    }

    @GetMapping("/getPushTopic/{userID}")
    ApiResponse<UserSubscribeResponse> getPushTopic(@PathVariable("userID") String userID) {
        return ApiResponse.<UserSubscribeResponse>builder()
                .result(new UserSubscribeResponse(notificationService.getPushTopicOfUser(userID)))
                .build();
    }

    @PostMapping("/subscribeToTopic")
    ApiResponse<String> subscribeToTopic(@RequestBody @Valid SubscribeTopicRequest subscribeTopicRequest) {
        return ApiResponse.<String>builder()
                .result(notificationService.subscribeToNewTopic(subscribeTopicRequest))
                .build();
    }
    @PostMapping("/unsubscribeToTopic")
    ApiResponse<String> unsubscribeToTopic(@RequestBody @Valid SubscribeTopicRequest subscribeTopicRequest) {
        return ApiResponse.<String>builder()
                .result(notificationService.unSubcribeToTopic(subscribeTopicRequest))
                .build();
    }


//    @PostMapping
//    ApiResponse<String> sendSpecificNotification(@RequestBody TopicNotificationRequest notification) {
//        return ApiResponse.<String>builder()
//                .data(notificationService.sendPushNotification(notification))
//                .build();
//    }

//    @KafkaListener(topics = "multipleUserNotification")
//    void listen(String message){
//        log.info("Message received: {}", message);
//    }
}
