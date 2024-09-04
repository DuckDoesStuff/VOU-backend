package com.vou.api.service;

import com.vou.api.entity.Notification;

public interface NotificationServiceType {
    String sendTopicNotification(Notification notification);
    String sendSpecificNotification(Notification notification);
}
