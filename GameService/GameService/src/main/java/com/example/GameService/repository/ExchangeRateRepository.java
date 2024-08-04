package com.example.GameService.repository;

// ExchangeRateRepository.java
import com.example.GameService.entity.ExchangeRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ExchangeRateRepository extends MongoRepository<ExchangeRate, String> {
    List<ExchangeRate> findByEventID(Long eventID);
    List<ExchangeRate> findByGameID(Long gameID);
    List<ExchangeRate> findByItemTypeID(Long itemTypeID);
    List<ExchangeRate> findByVoucherTypeID(Long voucherTypeID);
}
