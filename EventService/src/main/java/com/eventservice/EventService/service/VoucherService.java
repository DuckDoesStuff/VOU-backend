package com.eventservice.EventService.service;

import com.eventservice.EventService.dto.ApiResponse;
import com.eventservice.EventService.dto.VoucherDto;
import com.eventservice.EventService.entity.PromotionalEvent;
import com.eventservice.EventService.entity.VoucherType;
import com.eventservice.EventService.repository.PromotionalEventRepository;
import com.eventservice.EventService.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    PromotionalEventRepository promotionalEventRepository;

    public ResponseEntity<ApiResponse<List<VoucherDto>>> getVouchersByEvent(Long eventID) {
        PromotionalEvent event = promotionalEventRepository.getReferenceById(eventID);

        List<VoucherType> vouchers = voucherRepository.findByPromotionalEvent(event);

        List<VoucherDto> voucherDtos = vouchers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        ApiResponse<List<VoucherDto>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Vouchers fetched successfully");
        response.setResult(voucherDtos);

        // Return the response entity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<VoucherDto>> createVoucherForEvent(VoucherDto voucherDto) {
        // Fetch the promotional event based on the event ID from the DTO
        PromotionalEvent event = promotionalEventRepository.getReferenceById(voucherDto.getEventID());

        // Create a new VoucherType entity from the DTO
        VoucherType voucherType = VoucherType.builder()
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

        // Convert the saved VoucherType entity to a VoucherDto
        VoucherDto newVoucherDto = convertToDto(newVoucher);

        // Create and populate the ApiResponse with the VoucherDto
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

    // Conversion method from VoucherType to VoucherDto
    private VoucherDto convertToDto(VoucherType voucherType) {
        VoucherDto dto = new VoucherDto();
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
