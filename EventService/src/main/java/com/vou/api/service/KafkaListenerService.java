package com.vou.api.service;

import com.vou.api.dto.GiveUserVoucherMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaListenerService {

    private final VoucherService voucherService;

    @KafkaListener(topics = "give-user-voucher")
    public void listenToGiveUserVoucher(GiveUserVoucherMessage message) {
        System.out.println("Give player voucher in event service: " + message);
        UUID uuid = UUID.fromString(message.getUserID());
        voucherService.givePlayerVoucher(uuid, message.getVoucherTypeID());
    }
}