package com.example.ReportService.dto;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getNumUsed() {
        return numUsed;
    }

    public void setNumUsed(Integer numUsed) {
        this.numUsed = numUsed;
    }
}
