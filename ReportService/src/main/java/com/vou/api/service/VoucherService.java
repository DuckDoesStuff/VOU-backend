package com.vou.api.service;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.VoucherReportDTO;
import com.vou.api.dto.VoucherTypeMessage;
import com.vou.api.entity.VoucherType;
import com.vou.api.repository.VoucherTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherTypeRepository voucherTypeRepository;

    public ApiResponse<List<VoucherReportDTO>> getVouchersDistributedInAEvent(Long eventID) {
        List<VoucherReportDTO> reportDTOS = voucherTypeRepository.findVoucherReportByEventId(eventID);
        ApiResponse<List<VoucherReportDTO>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setResult(reportDTOS);
        return response;
    }
    public List<VoucherType> getAllVoucherType() {
        return voucherTypeRepository.findAll();
    }
    @KafkaListener(topics = "VoucherType-update")
    public void listenToVoucherChange(VoucherTypeMessage message) {
        Optional<VoucherType> voucherType = voucherTypeRepository.findById(message.getVoucherTypeID());
        System.out.println(message);
        if (voucherType.isEmpty()) {
            // create voucher type
            VoucherType newVoucher = VoucherType.builder()
                    .eventID(message.getEventID())
                    .voucherTypeID(message.getVoucherTypeID())
                    .gameID(message.getGameID())
                    .expiryDate(message.getExpiryDate())
                    .totalQuantity(message.getTotalQuantity())
                    .inStock(message.getInStock())
                    .build();
            voucherTypeRepository.save(newVoucher);
            System.out.println("save new voucher type");
            return;
        }
        // udpate voucher type
        VoucherType currentVoucherType = voucherType.get();
        currentVoucherType.setInStock(message.getInStock());
        voucherTypeRepository.save(currentVoucherType);
        System.out.println("Update current voucher type");
    }
}
