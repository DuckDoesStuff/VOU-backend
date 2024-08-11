package com.NotficationService.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    PushNotification("PushNotification"),
    EmailNotification("EmailNotification"),
    SmsNotification("SmsNotification");

    private NotificationType(String type) {
        this.NotificationType = type;
    }
    private String NotificationType;
}
