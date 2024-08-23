package com.vou.api.repository;

// ExchangeHistoryRepository.java
import com.vou.api.entity.ExchangeHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ExchangeHistoryRepository extends MongoRepository<ExchangeHistory, String> {
    List<ExchangeHistory> findByPresenterID(Long presenterID);
    List<ExchangeHistory> findByReceiverID(Long receiverID);
    List<ExchangeHistory> findByEventID(Long eventID);
    List<ExchangeHistory> findByGameID(Long gameID);
}
