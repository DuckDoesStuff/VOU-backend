package com.NotficationService.mapper;

import com.NotficationService.dto.request.PushTopicNotificationRequest;
import com.NotficationService.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(PushTopicNotificationRequest request);
}
