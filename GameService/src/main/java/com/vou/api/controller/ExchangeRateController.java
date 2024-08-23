package com.example.GameService.controller;

// ExchangeRateController.java
import com.example.GameService.dto.ApiResponse;
import com.example.GameService.dto.CreateExchangeRateDTO;
import com.example.GameService.dto.ExchangeForVoucherDTO;
import com.example.GameService.entity.ExchangeRate;
import com.example.GameService.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<CreateExchangeRateDTO>>> getExchangeRatesByEventID(@PathVariable Long eventID) {
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

    @PostMapping("/exchange_rate")
    public ResponseEntity<ApiResponse<CreateExchangeRateDTO>> saveExchangeRate(@RequestBody CreateExchangeRateDTO exchangeRate) {
        return exchangeRateService.saveExchangeRate(exchangeRate);
    }
    @PostMapping("/exchange_rate/voucher")
    public ResponseEntity<ApiResponse<String>> exchangeForVoucher(@RequestBody ExchangeForVoucherDTO exchangeForVoucherDTO) {
        return exchangeRateService.exchangeForVoucher(exchangeForVoucherDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteExchangeRate(@PathVariable String id) {
//        exchangeRateService.deleteExchangeRate(id);
    }

}
