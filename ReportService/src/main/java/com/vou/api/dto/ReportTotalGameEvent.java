package com.vou.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTotalGameEvent {
    private long totalEventsHosted;
    private long voucherDistributed;
    private long totalGame;
}
