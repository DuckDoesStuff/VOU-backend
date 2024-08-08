package com.eventservice.EventService.service;

import com.eventservice.EventService.entity.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherTypeRepository extends JpaRepository<VoucherType, Long> {
}
