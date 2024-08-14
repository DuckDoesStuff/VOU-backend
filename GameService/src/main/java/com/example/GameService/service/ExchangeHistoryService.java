package com.example.GameService.service;

// ExchangeHistoryService.java
import com.example.GameService.entity.ExchangeHistory;
import com.example.GameService.repository.ExchangeHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeHistoryService {
    @Autowired
    private ExchangeHistoryRepository exchangeHistoryRepository;

    public List<ExchangeHistory> getAllExchangeHistories() {
        return exchangeHistoryRepository.findAll();
    }

    public ExchangeHistory getExchangeHistory(String id) {
        return exchangeHistoryRepository.findById(id).orElse(null);
    }

    public List<ExchangeHistory> getExchangeHistoriesByPresenterID(Long presenterID) {
        return exchangeHistoryRepository.findByPresenterID(presenterID);
    }

    public List<ExchangeHistory> getExchangeHistoriesByReceiverID(Long receiverID) {
        return exchangeHistoryRepository.findByReceiverID(receiverID);
    }

    public List<ExchangeHistory> getExchangeHistoriesByEventID(Long eventID) {
        return exchangeHistoryRepository.findByEventID(eventID);
    }

    public List<ExchangeHistory> getExchangeHistoriesByGameID(Long gameID) {
        return exchangeHistoryRepository.findByGameID(gameID);
    }

    public ExchangeHistory saveExchangeHistory(ExchangeHistory exchangeHistory) {
        return exchangeHistoryRepository.save(exchangeHistory);
    }

    public void deleteExchangeHistory(String id) {
        exchangeHistoryRepository.deleteById(id);
    }
}
