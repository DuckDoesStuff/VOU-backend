package com.vou.api.repository;

import com.vou.api.dto.ReportTotalVoucherByBrandID;
import com.vou.api.dto.VoucherReportDTO;
import com.vou.api.entity.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherTypeRepository extends JpaRepository<VoucherType, Long> {

    @Query("SELECT new com.vou.api.dto.VoucherReportDTO(v.eventID, v.totalQuantity, v.totalQuantity - v.inStock) " +
            "FROM VoucherType v " +
            "WHERE v.eventID = :eventID")
    List<VoucherReportDTO> findVoucherReportByEventId(@Param("eventID") Long eventID);

    @Query("SELECT COALESCE(SUM(v.totalQuantity - v.inStock), 0) FROM VoucherType v")
    Long findTotalVouchersDistributed();

    @Query("SELECT new com.vou.api.dto.ReportTotalVoucherByBrandID(v.brandID, v.eventID, SUM(v.value * v.totalQuantity)) " +
            "FROM VoucherType v " +
            "GROUP BY v.brandID, v.eventID")
    List<ReportTotalVoucherByBrandID> findTotalValueGroupedByBrandAndEvent();
}
