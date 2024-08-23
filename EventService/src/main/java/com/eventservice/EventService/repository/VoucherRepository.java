package com.eventservice.EventService.repository;

import com.eventservice.EventService.entity.PromotionalEvent;
import com.eventservice.EventService.entity.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherType, Long> {
    List<VoucherType> findByPromotionalEvent(PromotionalEvent promotionalEvent);
    VoucherType findByVoucherTypeID(Long voucherTypeID);
}
