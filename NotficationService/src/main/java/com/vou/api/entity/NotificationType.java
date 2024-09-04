package com.vou.api.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    PushNotification("PushNotification"),
    SocketPushNotification("SocketPushNotification"),
    EmailNotification("EmailNotification"),
    SmsNotification("SmsNotification");

    private NotificationType(String type) {
        this.NotificationType = type;
    }
    private String NotificationType;
}
