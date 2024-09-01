package com.vou.api.service;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ReportTotalGameEvent;
import com.vou.api.repository.BrandStatisticsRepository;
import com.vou.api.repository.GameStatisticsRepository;
import com.vou.api.repository.VoucherTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportOverallService {
    private final BrandStatisticsRepository brandStatisticsRepository;
    private final GameStatisticsRepository gameStatisticsRepository;
    private final VoucherTypeRepository voucherTypeRepository;

    public ApiResponse<ReportTotalGameEvent> reportTotalGameEvent() {
        long totalEventsHosted = brandStatisticsRepository.findTotalEventsHosted();
        long totalGame = gameStatisticsRepository.findTotalGame();
        long voucherDistibuted = voucherTypeRepository.findTotalVouchersDistributed();
        ReportTotalGameEvent reportTotalGameEvent = ReportTotalGameEvent.builder()
                .totalEventsHosted(totalEventsHosted)
                .totalGame(totalGame)
                .voucherDistributed(voucherDistibuted)
                .build();
        ApiResponse<ReportTotalGameEvent> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setResult(reportTotalGameEvent);
        return response;
    }
}
