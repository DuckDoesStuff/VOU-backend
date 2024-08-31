package com.vou.api.service;

import com.vou.api.dto.VoucherTypeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "VoucherType-update";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendVoucherTypeMessage(VoucherTypeMessage message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
