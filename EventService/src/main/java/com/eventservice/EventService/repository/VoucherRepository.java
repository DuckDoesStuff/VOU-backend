package com.eventservice.EventService.repository;

import com.eventservice.EventService.entity.PromotionalEvent;
import com.eventservice.EventService.entity.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<VoucherType, Long> {
}
