package com.vou.api.controller;

// ExchangeRateController.java
import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.CombinedResult;
import com.vou.api.dto.CreateExchangeRateDTO;
import com.vou.api.dto.ExchangeForVoucherDTO;
import com.vou.api.entity.ExchangeRate;
import com.vou.api.service.ExchangeRateService;
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

    @GetMapping("/voucher/{voucherTypeID}/required")
    public ApiResponse<List<CombinedResult>> getRequiredItemTypeWithExchangeRate(@PathVariable Long voucherTypeID) {
        System.out.println("get required item type with exchange rate " + voucherTypeID);
        return exchangeRateService.getRequiredItemTypeWithExchangeRate(voucherTypeID);
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
        System.out.println("exchange for voucher controller " + exchangeForVoucherDTO);
        return exchangeRateService.exchangeForVoucher(exchangeForVoucherDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteExchangeRate(@PathVariable String id) {
//        exchangeRateService.deleteExchangeRate(id);
    }

}
