package com.example.ReportService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherStatistics {
    private Long eventID;
    private int totalQuantity;
    private int numberUsed;
}
