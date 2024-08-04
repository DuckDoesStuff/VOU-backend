package com.example.GameService.service;

// ExchangeRateService.java
import com.example.GameService.entity.ExchangeRate;
import com.example.GameService.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public ExchangeRate getExchangeRate(String id) {
        return exchangeRateRepository.findById(id).orElse(null);
    }

    public List<ExchangeRate> getExchangeRatesByEventID(Long eventID) {
        return exchangeRateRepository.findByEventID(eventID);
    }

    public List<ExchangeRate> getExchangeRatesByGameID(Long gameID) {
        return exchangeRateRepository.findByGameID(gameID);
    }

    public List<ExchangeRate> getExchangeRatesByItemTypeID(Long itemTypeID) {
        return exchangeRateRepository.findByItemTypeID(itemTypeID);
    }

    public List<ExchangeRate> getExchangeRatesByVoucherTypeID(Long voucherTypeID) {
        return exchangeRateRepository.findByVoucherTypeID(voucherTypeID);
    }

    public ExchangeRate saveExchangeRate(ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }

    public void deleteExchangeRate(String id) {
        exchangeRateRepository.deleteById(id);
    }
}

