package com.vou.api.service;

import com.vou.api.entity.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationServiceFactory {
    private final Map<String, NotificationServiceType> notificationServices = new HashMap<>();

    @Autowired
    public void NotificationServiceFactory(PushService pushService, SocketPushService socketPushService) {
        notificationServices.put(NotificationType.PushNotification.getNotificationType(), pushService);
        notificationServices.put(NotificationType.SocketPushNotification.getNotificationType(), socketPushService);
    }

    public NotificationServiceType getNotificationService(String serviceName) {
        return notificationServices.get(serviceName);
    }
}
