package com.vou.api.controller;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.DecreaseVoucherDTO;
import com.vou.api.dto.VoucherDto;
import com.vou.api.dto.VoucherResponse;
import com.vou.api.dto.response.GameScore;
import com.vou.api.entity.VoucherType;
import com.vou.api.entity.VoucherUser;
import com.vou.api.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/event/voucher")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VoucherType>>> getAllVouchers() {
        return voucherService.getAllVouchers();
    }

    @GetMapping("/{eventID}")
    public ResponseEntity<ApiResponse<List<VoucherType>>> getAllVouchersOfAnEvent(@PathVariable Long eventID) {
        return voucherService.getVouchersByEvent(eventID);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherDto>> createVoucher(@RequestBody @Valid VoucherDto voucherDto) {
        return voucherService.createVoucherForEvent(voucherDto);
    }

    @PostMapping("/{voucherID}")
    public ResponseEntity<ApiResponse<VoucherUser>> useVoucher(@PathVariable Long voucherID, @RequestParam("userID") UUID userID) {
        return voucherService.playerUseVoucher(userID, voucherID);
    }

    // /user/e3-abcd-xyzw?voucherID=123
    @PostMapping("/user/{userID}")
    public ResponseEntity<ApiResponse<VoucherUser>> giftVoucherToPlayer(@PathVariable UUID userID, @RequestParam("voucherID") Long voucherID) {
        return voucherService.givePlayerVoucher(userID, voucherID);
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<ApiResponse<List<VoucherUser>>> getVoucherByUserID(@PathVariable UUID userID) {
        return voucherService.getVoucherByUserID(userID);
    }

    @KafkaListener(topics = "SaveGameScore")
    public void receiveTopPlayers(GameScore gameScore) {
        voucherService.rewardTopPlayers(gameScore);
    }
}
