package com.vou.api.service;

// ExchangeRateService.java
import com.vou.api.dto.*;
import com.vou.api.entity.ExchangeRate;
import com.vou.api.entity.Item;
import com.vou.api.repository.ExchangeRateRepository;
import com.vou.api.repository.ItemRepository;
import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.CreateExchangeRateDTO;
import com.vou.api.dto.DecreaseQuantityDTO;
import com.vou.api.dto.ExchangeForVoucherDTO;
import com.vou.api.entity.ExchangeRate;
import com.vou.api.entity.Item;
import com.vou.api.repository.ExchangeRateRepository;
import com.vou.api.repository.ItemRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private ItemRepository itemRepository;
    private final RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);


    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public ExchangeRate getExchangeRate(String id) {
        return exchangeRateRepository.findById(id).orElse(null);
    }

    public ResponseEntity<ApiResponse<List<CreateExchangeRateDTO>>> getExchangeRatesByEventID(Long eventID) {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByEventID(eventID);
//        if (exchangeRates.isEmpty()) {
//            throw new ResourceNotFoundException("No exchange rates found for event ID: " + eventID);
//        }
        List<CreateExchangeRateDTO> exchangeRateDTOS = exchangeRates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        ApiResponse<List<CreateExchangeRateDTO>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get all exchange rates of an event");
        response.setResult(exchangeRateDTOS);
        return new ResponseEntity<>(
            response,
            HttpStatus.OK
        );
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

    public ResponseEntity<ApiResponse<CreateExchangeRateDTO>> saveExchangeRate(CreateExchangeRateDTO exchangeRateDTO) {
        // Convert DTO to entity
        ExchangeRate exchangeRate = convertToEntity(exchangeRateDTO);

        // Save the exchange rate in the database
        ExchangeRate savedExchangeRate = exchangeRateRepository.save(exchangeRate);

        // Convert the saved entity back to DTO
        CreateExchangeRateDTO savedExchangeRateDTO = convertToDTO(savedExchangeRate);

        // Create and populate the ApiResponse
        ApiResponse<CreateExchangeRateDTO> response = new ApiResponse<>();
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Exchange rate created successfully");
        response.setResult(savedExchangeRateDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Conversion from DTO to Entity
    private ExchangeRate convertToEntity(CreateExchangeRateDTO exchangeRateDTO) {
        ExchangeRate exchangeRate = new ExchangeRate();
        if (exchangeRate.getId() == null) {
            exchangeRate.setId(new ObjectId());
        }
        exchangeRate.setEventID(exchangeRateDTO.getEventID());
        exchangeRate.setGameID(exchangeRateDTO.getGameID());
        exchangeRate.setItemTypeID(exchangeRateDTO.getItemTypeID());
        exchangeRate.setVoucherTypeID(exchangeRateDTO.getVoucherTypeID());
        exchangeRate.setExchangeRate(exchangeRateDTO.getExchangeRate());
        return exchangeRate;
    }

    // Conversion from Entity to DTO
    private CreateExchangeRateDTO convertToDTO(ExchangeRate exchangeRate) {
        return CreateExchangeRateDTO.builder()
                .eventID(exchangeRate.getEventID())
                .gameID(exchangeRate.getGameID())
                .itemTypeID(exchangeRate.getItemTypeID())
                .voucherTypeID(exchangeRate.getVoucherTypeID())
                .exchangeRate(exchangeRate.getExchangeRate())
                .build();
    }

    public ResponseEntity<ApiResponse<String>> exchangeForVoucher(ExchangeForVoucherDTO exchangeForVoucherDTO) {
        // User want to exchange for a voucher
        String userID = exchangeForVoucherDTO.getUserID();
        ObjectId gameID = exchangeForVoucherDTO.getGameID();
        ObjectId itemTypeID = exchangeForVoucherDTO.getItemTypeID();
        // User id, voucher (voucher type id, game id, event id, exchangeRate)
        // 1. Check if user qualify for this voucher
        ApiResponse<String> response = new ApiResponse<>();
        Item item = itemRepository.findByItemTypeIDAndUserID(exchangeForVoucherDTO.getItemTypeID(), userID);

        if (item.getQuantity() < exchangeForVoucherDTO.getExchangeRate()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("You do not have enough items to exchange for vouchers");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
            // 1.1 Get item of user by item type id for that voucher
            // 1.2 Check if quantity is enough for exchangeRate
                // 1.2.1 If yes, decrease in stock and decrease quantity in user items
        item.setQuantity(item.getQuantity() - exchangeForVoucherDTO.getExchangeRate());
        // call event service to decrease voucher type totalQuantity
        String url = "http://localhost:8003/event/voucher/decrease_quantity";
        DecreaseQuantityDTO voucherRequest = new DecreaseQuantityDTO();
        voucherRequest.setVoucherTypeID(exchangeForVoucherDTO.getVoucherTypeID());
        voucherRequest.setTotalDecreased(exchangeForVoucherDTO.getExchangeRate());
        HttpEntity<DecreaseQuantityDTO> requestEntity = new HttpEntity<>(voucherRequest);

        ResponseEntity<ApiResponse<String>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ApiResponse<String>>() {}
        );
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Cannot exchange this voucher");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        itemRepository.save(item);
        response.setMessage("You have exchange this voucher successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public void deleteExchangeRate(String id) {
        exchangeRateRepository.deleteById(id);
    }
}

