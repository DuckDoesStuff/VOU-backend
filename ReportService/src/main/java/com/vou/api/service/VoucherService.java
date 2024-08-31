package com.vou.api.service;

import com.vou.api.dto.ApiResponse;
import com.vou.api.dto.VoucherReportDTO;
import com.vou.api.repository.VoucherTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherTypeRepository voucherTypeRepository;

    public ApiResponse<List<VoucherReportDTO>> getVouchersDistributedInAEvent(Long eventID) {
        List<VoucherReportDTO> reportDTOS = voucherTypeRepository.findVoucherReportByEventId(eventID);
        ApiResponse<List<VoucherReportDTO>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setResult(reportDTOS);
        return response;
    }
}
