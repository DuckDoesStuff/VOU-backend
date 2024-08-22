package com.vou.api.dto.stream;

import lombok.Getter;

@Getter
public enum StreamEvent {
    START_STREAM,
    QUESTION,
    ANSWER,
    STOP_STREAM,
}
