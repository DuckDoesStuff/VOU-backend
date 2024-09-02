package com.vou.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ReportTotalVoucherByBrandID {
    private String brandID;
    private Long eventID;
    private Double totalValue;

    public ReportTotalVoucherByBrandID(String brandID, Long eventID, Double totalValue) {
        this.brandID = brandID;
        this.eventID = eventID;
        this.totalValue = totalValue;
    }
}
