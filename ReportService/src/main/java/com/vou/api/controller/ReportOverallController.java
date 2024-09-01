package com.vou.api.controller;

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

    @GetMapping("/overall/count_game_event")
    public void findTotalGameEvent() {
        reportOverallService.reportTotalGameEvent();
    }
}
