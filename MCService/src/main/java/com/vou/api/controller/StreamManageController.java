package com.vou.api.controller;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.service.StreamService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;

@Component
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE)
public class StreamManageController {
    @Autowired
    PropertyChangeListener streamService;

    @KafkaListener(topics = "startStream")
    public void listenStartStreamRequest(InfoForStream message) {
        log.info("InfoForStream: {}", message);
        ((StreamService)streamService).startStream(message);
    }
}
