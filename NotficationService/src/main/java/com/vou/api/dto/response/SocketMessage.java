package com.vou.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class SocketMessage {
    String tittle;
    String content;
    String time;
}
