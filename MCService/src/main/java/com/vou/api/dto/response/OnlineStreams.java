package com.vou.api.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

@Builder
@Data
public class OnlineStreams {
    @Value("${rtmp.server}")
    private String server;
    private Set<String> roomIDs;
}
