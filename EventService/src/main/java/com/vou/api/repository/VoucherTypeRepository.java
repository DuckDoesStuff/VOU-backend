package com.vou.api.repository;

import com.vou.api.entity.PromotionalEvent;
import com.vou.api.entity.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherTypeRepository extends JpaRepository<VoucherType, Long> {
    List<VoucherType> findByPromotionalEvent(PromotionalEvent promotionalEvent);
    VoucherType findByVoucherTypeID(Long voucherTypeID);
    Optional<List<VoucherType>> findByGameID(String gameID);
}
