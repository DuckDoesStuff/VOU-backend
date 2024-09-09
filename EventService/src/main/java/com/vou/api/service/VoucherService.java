package com.vou.api.service;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.VoucherDto;
import com.vou.api.dto.VoucherTypeMessage;
import com.vou.api.dto.response.GameScore;
import com.vou.api.entity.PromotionalEvent;
import com.vou.api.entity.UserScore;
import com.vou.api.entity.VoucherType;
import com.vou.api.entity.VoucherUser;
import com.vou.api.exception.ErrorCode;
import com.vou.api.exception.VoucherException;
import com.vou.api.repository.PromotionalEventRepository;
import com.vou.api.repository.VoucherTypeRepository;
import com.vou.api.repository.VoucherUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherTypeRepository voucherTypeRepository;
    private final VoucherUserRepository voucherUserRepository;
    private final KafkaProducerService kafkaProducerService;

    private final PromotionalEventRepository promotionalEventRepository;

    public ResponseEntity<ApiResponse<List<VoucherType>>> getVouchersByEvent(Long eventID) {
        PromotionalEvent event = promotionalEventRepository.getReferenceById(eventID);

        List<VoucherType> vouchers = voucherTypeRepository.findByPromotionalEvent(event);

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
        if (voucherDto.getCode() != null) {
            voucherType.setCode(voucherDto.getCode());
            System.out.println("Voucher code" + voucherDto.getCode());
        }
        System.out.println("Voucher code" + voucherDto.getCode());
        // Save the new voucher in the repository
        VoucherType newVoucher = voucherTypeRepository.save(voucherType);

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
        response.setMessage("VoucherUser created successfully");
        response.setResult(newVoucherDto);

        // Return the response entity with the DTO
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public List<VoucherType> getVoucherFromEventID(Long eventID) {
        PromotionalEvent event = promotionalEventRepository.getReferenceById(eventID);

        return voucherTypeRepository.findByPromotionalEvent(event);
    }

    public ResponseEntity<ApiResponse<List<VoucherType>>> getAllVouchers() {
        List<VoucherType> vouchers = voucherTypeRepository.findAll();

        // Create and populate ApiResponse with the VoucherDto list
        ApiResponse<List<VoucherType>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get all vouchers");
        response.setResult(vouchers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<String>> decreaseTotalQuantity(Long voucherTypeID, int totalDecreased) {
        VoucherType voucherType = voucherTypeRepository.findByVoucherTypeID(voucherTypeID);
        voucherType.setInStock(voucherType.getInStock() - totalDecreased);
        voucherTypeRepository.save(voucherType);
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

    // Give player a voucher
    public ResponseEntity<ApiResponse<VoucherUser>> givePlayerVoucher(UUID userID, Long voucherTypeID) {
        VoucherType voucherType = voucherTypeRepository.findById(voucherTypeID).orElseThrow(() -> new VoucherException(ErrorCode.VOUCHER_NOT_FOUND)); // Should throw a specific exception

        VoucherUser voucherUser = new VoucherUser();
        voucherUser.setUserID(userID);
        voucherUser.setVoucherType(voucherType);
        voucherUser.setUsed(false);

        // If this is QR Coupon then decrease the stock
        if (voucherType.getCode() == null) {
            if (voucherType.getInStock() > 0) voucherType.setInStock(voucherType.getInStock() - 1);
            else throw new VoucherException(ErrorCode.VOUCHER_OUT_OF_STOCK);

            // Generate unique coupon key
            voucherUser.setCodeQR(voucherType.getVoucherTypeID() + "_" + voucherType.getPromotionalEvent().getEventID() + "_" + userID.toString());

            // Send to kafka topic VoucherType-update
            VoucherTypeMessage message = VoucherTypeMessage.builder()
                    .eventID(voucherType.getPromotionalEvent().getEventID())
                    .voucherTypeID(voucherType.getVoucherTypeID())
                    .inStock(voucherType.getInStock())
                    .build();
            System.out.println(message.toString());
            kafkaProducerService.sendVoucherTypeMessage(message);
        } else {
            // If this is code voucher, check if the user has already "saved" it
            List<VoucherUser> vouchers = voucherUserRepository.findByUserID(userID);
            for (VoucherUser vou : vouchers) {
                if (vou.getVoucherType() == voucherType)
                    throw new VoucherException(ErrorCode.VOUCHER_ALREADY_SAVED);
            }
        }

        ApiResponse<VoucherUser> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setMessage("Voucher gifted to user");
        apiResponse.setResult(voucherUserRepository.save(voucherUser));

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    // Player use voucher
    public ResponseEntity<ApiResponse<VoucherUser>> playerUseVoucher(UUID userID, Long voucherUserID) {
        if (userID == null) throw new VoucherException(ErrorCode.VOUCHER_NOT_FOUND);
        VoucherUser voucherUser = voucherUserRepository.findById(voucherUserID).orElseThrow(() -> new VoucherException(ErrorCode.VOUCHER_NOT_FOUND));

        if (!userID.equals(voucherUser.getUserID())) throw new VoucherException(ErrorCode.VOUCHER_NOT_FOUND);
        if (voucherUser.isUsed()) throw new VoucherException(ErrorCode.VOUCHER_USED);
        VoucherType voucherType = voucherUser.getVoucherType();

        // Check expiry
        if (LocalDateTime.now().isAfter(voucherType.getExpiryDay()))
            throw new VoucherException(ErrorCode.VOUCHER_EXPIRED);

        // Check availability
        // If this is Coupon voucher
        if (voucherUser.getCodeQR() != null) {
            voucherUser.setUsed(true);
        } else {
            voucherType.setInStock(voucherType.getInStock() - 1);
            voucherUser.setUsed(true);
            voucherTypeRepository.save(voucherType);
            // Send to kafka topic VoucherType-update
            VoucherTypeMessage message = VoucherTypeMessage.builder()
                    .eventID(voucherType.getPromotionalEvent().getEventID())
                    .voucherTypeID(voucherType.getVoucherTypeID())
                    .inStock(voucherType.getInStock())
                    .build();
            System.out.println(message.toString());
            kafkaProducerService.sendVoucherTypeMessage(message);
        }

        ApiResponse<VoucherUser> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setMessage("Voucher used successfully");
        apiResponse.setResult(voucherUserRepository.save(voucherUser));
        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );
    }

    // Get vouchers by player
    public ResponseEntity<ApiResponse<List<VoucherUser>>> getVoucherByUserID(UUID userID) {
        List<VoucherUser> vouchers = voucherUserRepository.findByUserID(userID);

        ApiResponse<List<VoucherUser>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("User vouchers");
        apiResponse.setResult(vouchers);
        apiResponse.setStatus(HttpStatus.OK.value());

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.OK
        );

    }

    public void rewardTopPlayers(GameScore gameScore) {
        List<VoucherType> voucherType = voucherTypeRepository.findByGameID(gameScore.getGameID()).orElseThrow(() -> new VoucherException(ErrorCode.VOUCHER_NOT_FOUND));
        if (voucherType.isEmpty()) return;
        List<UserScore> userScores = gameScore.getUserScores();

        int i = 1;
        VoucherType voucher = voucherType.getFirst();
        for (UserScore userScore : userScores) {
            try {
                givePlayerVoucher(UUID.fromString(userScore.getUserID()), voucher.getVoucherTypeID());
            } catch (VoucherException e) {
                switch (e.getErrorCode()) {
                    case ErrorCode.VOUCHER_OUT_OF_STOCK:
                        voucher = voucherType.get(i);
                        i++;
                        givePlayerVoucher(UUID.fromString(userScore.getUserID()), voucher.getVoucherTypeID());
                        break;
                    case ErrorCode.VOUCHER_ALREADY_SAVED:
                        givePlayerVoucher(UUID.fromString(userScore.getUserID()), voucherType.get(i+1).getVoucherTypeID());
                        break;
                    default:
                        i++;
                        break;
                }
            } catch (Exception e) {
                log.warn("Rewarding player: Failed to give player {} voucher", userScore.getUserID());
            }
        }
    }
}