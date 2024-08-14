package com.example.GameService.controller;

// ExchangeRateController.java
import com.example.GameService.entity.ExchangeRate;
import com.example.GameService.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exchange_rates")
public class ExchangeRateController {
    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateService.getAllExchangeRates();
    }

    @GetMapping("/{id}")
    public ExchangeRate getExchangeRate(@PathVariable String id) {
        return exchangeRateService.getExchangeRate(id);
    }

    @GetMapping("/event/{eventID}")
    public List<ExchangeRate> getExchangeRatesByEventID(@PathVariable Long eventID) {
        return exchangeRateService.getExchangeRatesByEventID(eventID);
    }

    @GetMapping("/game/{gameID}")
    public List<ExchangeRate> getExchangeRatesByGameID(@PathVariable Long gameID) {
        return exchangeRateService.getExchangeRatesByGameID(gameID);
    }

    @GetMapping("/item/{itemTypeID}")
    public List<ExchangeRate> getExchangeRatesByItemTypeID(@PathVariable Long itemTypeID) {
        return exchangeRateService.getExchangeRatesByItemTypeID(itemTypeID);
    }

    @GetMapping("/voucher/{voucherTypeID}")
    public List<ExchangeRate> getExchangeRatesByVoucherTypeID(@PathVariable Long voucherTypeID) {
        return exchangeRateService.getExchangeRatesByVoucherTypeID(voucherTypeID);
    }

    @PostMapping
    public ExchangeRate saveExchangeRate(@RequestBody ExchangeRate exchangeRate) {
        return exchangeRateService.saveExchangeRate(exchangeRate);
    }

    @DeleteMapping("/{id}")
    public void deleteExchangeRate(@PathVariable String id) {
//        exchangeRateService.deleteExchangeRate(id);
    }
}
