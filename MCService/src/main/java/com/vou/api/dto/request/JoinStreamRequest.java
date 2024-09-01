package com.vou.api.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinStreamRequest {
    String userID;
    String eventID;
    String roomID;
}
