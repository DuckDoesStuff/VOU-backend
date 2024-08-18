package com.eventservice.EventService.service;

import com.eventservice.EventService.dto.VoucherDto;
import com.eventservice.EventService.entity.PromotionalEvent;
import com.eventservice.EventService.entity.VoucherType;
import com.eventservice.EventService.repository.PromotionalEventRepository;
import com.eventservice.EventService.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    PromotionalEventRepository promotionalEventRepository;

    public VoucherType createVoucherForEvent(Long eventID, VoucherDto voucherDto) {
        PromotionalEvent event = promotionalEventRepository.getReferenceById(eventID);
        VoucherType voucherType = VoucherType.builder()
                .promotionalEvent(event)
                .totalQuantity(voucherDto.getTotalQuantity())
                .inStock(voucherDto.getTotalQuantity())
                .nameOfVoucher(voucherDto.getNameOfVoucher())
                .value(voucherDto.getValue())
                .build();
        return voucherRepository.save(voucherType);
    }

    public List<VoucherType> getVoucherFromEventID(Long eventID) {
        PromotionalEvent event = promotionalEventRepository.getReferenceById(eventID);

        return voucherRepository.findByPromotionalEvent(event);
    }
}
