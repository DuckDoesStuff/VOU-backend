package com.vou.api.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinRoomResponse<T> {
    int code = 200;
    String message = "Success";
    boolean success;
    private T data;
}
