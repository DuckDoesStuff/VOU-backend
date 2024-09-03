package com.vou.api.dto.response;


import com.vou.api.entity.Notification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserNotificationReponse {
    ArrayList<Notification> notifications;
}
