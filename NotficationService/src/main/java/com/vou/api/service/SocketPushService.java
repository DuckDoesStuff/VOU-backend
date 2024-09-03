package com.vou.api.service;


import com.google.firebase.messaging.Message;
import com.vou.api.dto.response.SocketMessage;
import com.vou.api.entity.Notification;
import com.vou.api.exception.AppException;
import com.vou.api.exception.ErrorCode;
import com.vou.api.socket.manage.SocketHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocketPushService implements NotificationServiceType {
    @Autowired
    private SocketHandler socketHandler;

    @Override
    public String sendTopicNotification(Notification notification) throws AppException {
//        System.out.print(notification.getTopic());
//        System.out.print(notification.getContent());
//        System.out.print(notification.getTitle());
//        System.out.println("ddd");


        SocketMessage socketMessage = SocketMessage.builder()
                .tittle(notification.getTitle())
                .content(notification.getContent())
                .time(notification.getTime())
                .build();
        String response = null;
        try {
//            response = firebaseMessaging.send(message);
//            System.out.println(response);
            socketHandler.sendRoomMessage(notification.getTopic(),"notification",socketMessage);
            return "Successfully send notification to " + notification.getTopic();
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_SEND_PUSHMESSAGE);
        }
    }

    @Override
    public String sendSpecificNotification(Notification notification) {
        return "";
    }


}
