package com.NotficationService.service;

import com.NotficationService.entity.Notification;

public interface NotificationServiceType {
    String sendTopicNotification(Notification notification);
    String sendSpecificNotification(Notification notification);
}
