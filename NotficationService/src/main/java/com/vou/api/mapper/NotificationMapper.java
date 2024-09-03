package com.vou.api.mapper;

import com.vou.api.dto.request.PushTopicNotificationRequest;
import com.vou.api.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(PushTopicNotificationRequest request);
}
