package com.vou.api.service;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.VoucherDto;
import com.vou.api.dto.VoucherTypeMessage;
import com.vou.api.entity.PromotionalEvent;
import com.vou.api.entity.VoucherType;
import com.vou.api.repository.PromotionalEventRepository;
import com.vou.api.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final KafkaProducerService kafkaProducerService;

    private final PromotionalEventRepository promotionalEventRepository;

    public ResponseEntity<ApiResponse<List<VoucherType>>> getVouchersByEvent(Long eventID) {
        PromotionalEvent event = promotionalEventRepository.getReferenceById(eventID);

        List<VoucherType> vouchers = voucherRepository.findByPromotionalEvent(event);

        ApiResponse<List<VoucherType>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Vouchers fetched successfully");
        response.setResult(vouchers);

        // Return the response entity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<VoucherDto>> createVoucherForEvent(VoucherDto voucherDto) {
        // Fetch the promotional event based on the event ID from the DTO
        PromotionalEvent event = promotionalEventRepository.getReferenceById(voucherDto.getEventID());

        // Create a new VoucherType entity from the DTO
        VoucherType voucherType = VoucherType.builder()
                .gameID(voucherDto.getGameID())
                .promotionalEvent(event)
                .totalQuantity(voucherDto.getTotalQuantity())
                .inStock(voucherDto.getTotalQuantity())
                .nameOfVoucher(voucherDto.getNameOfVoucher())
                .value(voucherDto.getValue())
                .expiryDay(voucherDto.getExpiryDate())  // Assuming you want to include the expiry date
                .picture(voucherDto.getPicture())
                .description(voucherDto.getDescription())
                .build();

        // Save the new voucher in the repository
        VoucherType newVoucher = voucherRepository.save(voucherType);

        // Send this new voucher to kafka topic VoucherType-update
        VoucherTypeMessage message = VoucherTypeMessage.builder()
                .gameID(newVoucher.getGameID())
                .expiryDate(newVoucher.getExpiryDay())
                .totalQuantity(newVoucher.getTotalQuantity())
                .inStock(newVoucher.getTotalQuantity())
                .voucherTypeID(newVoucher.getVoucherTypeID())
                .eventID(newVoucher.getPromotionalEvent().getEventID())
                .build();
        kafkaProducerService.sendVoucherTypeMessage(message);
        System.out.println("Send kafka topic " + message.toString());

        // Convert the saved VoucherType entity to a VoucherDto
        VoucherDto newVoucherDto = convertToDto(newVoucher);

        ApiResponse<VoucherDto> response = new ApiResponse<>();
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Voucher created successfully");
        response.setResult(newVoucherDto);

        // Return the response entity with the DTO
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public List<VoucherType> getVoucherFromEventID(Long eventID) {
        PromotionalEvent event = promotionalEventRepository.getReferenceById(eventID);

        return voucherRepository.findByPromotionalEvent(event);
    }

    public ResponseEntity<ApiResponse<List<VoucherDto>>> getAllVouchers() {
        List<VoucherType> vouchers = voucherRepository.findAll();

        // Convert each VoucherType to VoucherDto
        List<VoucherDto> voucherDtos = vouchers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Create and populate ApiResponse with the VoucherDto list
        ApiResponse<List<VoucherDto>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get all vouchers");
        response.setResult(voucherDtos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<String>> decreaseTotalQuantity(Long voucherTypeID, int totalDecreased) {
        VoucherType voucherType = voucherRepository.findByVoucherTypeID(voucherTypeID);
        voucherType.setInStock(voucherType.getInStock() - totalDecreased);
        voucherRepository.save(voucherType);
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Decreased voucher quantity successfully");
        // Send to kafka topic VoucherType-update
        VoucherTypeMessage message = VoucherTypeMessage.builder()
                .eventID(voucherType.getPromotionalEvent().getEventID())
                .voucherTypeID(voucherType.getVoucherTypeID())
                .inStock(voucherType.getInStock())
                .build();
        System.out.println(message.toString());
        kafkaProducerService.sendVoucherTypeMessage(message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Conversion method from VoucherType to VoucherDto
    private VoucherDto convertToDto(VoucherType voucherType) {
        VoucherDto dto = new VoucherDto();
        dto.setGameID(voucherType.getGameID());
        dto.setEventID(voucherType.getPromotionalEvent().getEventID());
        dto.setTotalQuantity(voucherType.getTotalQuantity());
        dto.setExpiryDate(voucherType.getExpiryDay());
        dto.setNameOfVoucher(voucherType.getNameOfVoucher());
        dto.setPicture(voucherType.getPicture());
        dto.setDescription(voucherType.getDescription());
        dto.setValue(voucherType.getValue());
        return dto;
    }
}
