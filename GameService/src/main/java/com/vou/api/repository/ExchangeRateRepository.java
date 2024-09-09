package com.vou.api.repository;

// ExchangeRateRepository.java
import com.vou.api.dto.CombinedResult;
import com.vou.api.entity.ExchangeRate;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ExchangeRateRepository extends MongoRepository<ExchangeRate, String> {
    List<ExchangeRate> findByEventID(Long eventID);
    List<ExchangeRate> findByGameID(Long gameID);
    List<ExchangeRate> findByItemTypeID(Long itemTypeID);
    List<ExchangeRate> findByVoucherTypeID(Long voucherTypeID);

    @Aggregation(pipeline = {
            "{ '$match': { 'voucherTypeID': ?0 } }",  // Match based on eventID
            "{ '$lookup': { 'from': 'item_types', 'localField': 'itemTypeID', 'foreignField': '_id', 'as': 'itemTypeDetails' } }",
            "{ '$unwind': '$itemTypeDetails' }",  // Flatten the results
            "{ '$project': { " +
                    "    'eventID': 1, " +
                    "    'gameID': 1, " +
                    "    'itemTypeID': 1, " +
                    "    'voucherTypeID': 1, " +
                    "    'exchangeRate': 1, " +
                    "    'itemTypeDetails': 1" +
                    "} }"
    })
    List<CombinedResult> getCombinedResultByEventID(Long voucherTypeID);
}
