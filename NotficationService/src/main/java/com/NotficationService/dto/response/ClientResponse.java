package com.NotficationService.dto.response;


import com.NotficationService.entity.Notification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientResponse {
    ArrayList<Notification> notifications;
}
