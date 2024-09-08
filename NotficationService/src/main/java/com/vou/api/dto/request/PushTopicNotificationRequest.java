package com.vou.api.dto.request;


import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PushTopicNotificationRequest {
    String topic;
//    @Size(max = 20, message = "INVALID_TITLE")
    String title;
    String content;
    ArrayList<String> receiver;
}
