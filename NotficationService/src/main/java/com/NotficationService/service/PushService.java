package com.NotficationService.service;

import com.NotficationService.entity.Notification;
import com.NotficationService.exception.AppException;
import com.NotficationService.exception.ErrorCode;
import com.google.firebase.messaging.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PushService implements NotificationServiceType {

    @Override
    public String sendTopicNotification(Notification notification) throws AppException {
        Message message = Message.builder()
                .setTopic(notification.getTopic())
                .putData("title", notification.getTitle())
                .putData("content", notification.getContent())
                .putData("time",notification.getDate().toString())
                .build();
        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
            return response;
        } catch (FirebaseMessagingException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_PUSHMESSAGE);
        }
    }

    @Override
    public String sendSpecificNotification(Notification notification) {
        return "";
    }


}
