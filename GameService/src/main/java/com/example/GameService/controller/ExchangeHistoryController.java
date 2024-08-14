package com.example.GameService.controller;

// ExchangeHistoryController.java
import com.example.GameService.entity.ExchangeHistory;
import com.example.GameService.service.ExchangeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exchange_history")
public class ExchangeHistoryController {
    @Autowired
    private ExchangeHistoryService exchangeHistoryService;

    @GetMapping
    public List<ExchangeHistory> getAllExchangeHistories() {
        return exchangeHistoryService.getAllExchangeHistories();
    }

    @GetMapping("/{id}")
    public ExchangeHistory getExchangeHistory(@PathVariable String id) {
        return exchangeHistoryService.getExchangeHistory(id);
    }

    @GetMapping("/presenter/{presenterID}")
    public List<ExchangeHistory> getExchangeHistoriesByPresenterID(@PathVariable Long presenterID) {
        return exchangeHistoryService.getExchangeHistoriesByPresenterID(presenterID);
    }

    @GetMapping("/receiver/{receiverID}")
    public List<ExchangeHistory> getExchangeHistoriesByReceiverID(@PathVariable Long receiverID) {
        return exchangeHistoryService.getExchangeHistoriesByReceiverID(receiverID);
    }

    @GetMapping("/event/{eventID}")
    public List<ExchangeHistory> getExchangeHistoriesByEventID(@PathVariable Long eventID) {
        return exchangeHistoryService.getExchangeHistoriesByEventID(eventID);
    }

    @GetMapping("/game/{gameID}")
    public List<ExchangeHistory> getExchangeHistoriesByGameID(@PathVariable Long gameID) {
        return exchangeHistoryService.getExchangeHistoriesByGameID(gameID);
    }

    @PostMapping
    public ExchangeHistory saveExchangeHistory(@RequestBody ExchangeHistory exchangeHistory) {
        return exchangeHistoryService.saveExchangeHistory(exchangeHistory);
    }

    @DeleteMapping("/{id}")
    public void deleteExchangeHistory(@PathVariable String id) {
        exchangeHistoryService.deleteExchangeHistory(id);
    }
}
