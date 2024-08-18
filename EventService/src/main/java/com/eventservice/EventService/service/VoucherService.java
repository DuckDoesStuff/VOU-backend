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

        return null;
    }

    public List<VoucherType> getVoucherFromEventID(Long eventID) {
        PromotionalEvent event = promotionalEventRepository.getReferenceById(eventID);

//        return voucherRepository.findAllVoucherFromEvent(event);
        return null;
    }
}
