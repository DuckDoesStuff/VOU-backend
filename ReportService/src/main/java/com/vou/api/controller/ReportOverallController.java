package com.vou.api.controller;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.ReportTotalGameEvent;
import com.vou.api.service.ReportOverallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportOverallController {
    private final ReportOverallService reportOverallService;

    @GetMapping("/overall/count-game-event")
    public ApiResponse<ReportTotalGameEvent> findTotalGameEvent() {
        return reportOverallService.reportTotalGameEvent();
    }
}
