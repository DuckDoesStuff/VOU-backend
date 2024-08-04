package com.NotficationService.service;

import com.NotficationService.entity.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationServiceFactory {
    private final Map<String, NotificationServiceType> notificationServices = new HashMap<>();

    public void NotificationServiceFactory(PushService pushService) {
        notificationServices.put(NotificationType.PushNotification.getNotificationType(), pushService);
    }

    public NotificationServiceType getNotificationService(String serviceName) {
        return notificationServices.get(serviceName);
    }
}
