package com.vou.api.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, T message) {
        kafkaTemplate.send(topic, message);
    }
}