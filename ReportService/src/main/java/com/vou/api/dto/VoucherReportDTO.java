package com.vou.api.dto;

import lombok.Getter;

@Getter
public class VoucherReportDTO {

    private Long eventId;
    private Integer totalQuantity;
    private Integer numUsed;

    public VoucherReportDTO(Long eventId, Integer totalQuantity, Integer numUsed) {
        this.eventId = eventId;
        this.totalQuantity = totalQuantity;
        this.numUsed = numUsed;
    }

    // Getters and Setters

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setNumUsed(Integer numUsed) {
        this.numUsed = numUsed;
    }
}
