package com.example.ReportService.repository;

import com.example.ReportService.dto.VoucherReportDTO;
import com.example.ReportService.entity.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherTypeRepository extends JpaRepository<VoucherType ,Long> {
    @Query("SELECT new VoucherReportDTO(v.eventId, v.totalQuantity, v.totalQuantity - v.inStock) " +
            "FROM VoucherType v " +
            "WHERE v.eventId = :eventId")
    List<VoucherReportDTO> findVoucherReportByEventId(@Param("eventId") Long eventId);

}
