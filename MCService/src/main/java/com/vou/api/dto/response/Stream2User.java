package com.vou.api.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stream2User {
    String eventID;
    String eventName;
    String roomID;
    String gameName;
    String banner;
}
