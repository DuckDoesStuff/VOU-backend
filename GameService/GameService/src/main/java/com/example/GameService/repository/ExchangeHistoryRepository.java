package com.example.GameService.repository;

// ExchangeHistoryRepository.java
import com.example.GameService.entity.ExchangeHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ExchangeHistoryRepository extends MongoRepository<ExchangeHistory, String> {
    List<ExchangeHistory> findByPresenterID(Long presenterID);
    List<ExchangeHistory> findByReceiverID(Long receiverID);
    List<ExchangeHistory> findByEventID(Long eventID);
    List<ExchangeHistory> findByGameID(Long gameID);
}
