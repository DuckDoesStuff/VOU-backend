package com.vou.api.repository;

import com.vou.api.dto.VoucherReportDTO;
import com.vou.api.entity.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherTypeRepository extends JpaRepository<VoucherType ,Long> {
    @Query("SELECT new com.example.ReportService.dto.VoucherReportDTO(v.eventID, v.totalQuantity, v.totalQuantity - v.inStock) " +
            "FROM VoucherType v " +
            "WHERE v.eventID = :eventID")
    List<VoucherReportDTO> findVoucherReportByEventId(@Param("eventID") Long eventID);

}
