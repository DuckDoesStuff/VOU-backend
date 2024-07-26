package com.example.demo.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "UserVoucher")
public class UserVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voucherID;

    @Lob
    private String QR;
    private Boolean state;
    private Integer eventID;
    private Integer gameID;
    private Integer voucherTypeID;
    private Integer userID;

    // Getters and Setters

    public Integer getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(Integer voucherID) {
        this.voucherID = voucherID;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public Integer getVoucherTypeID() {
        return voucherTypeID;
    }

    public void setVoucherTypeID(Integer voucherTypeID) {
        this.voucherTypeID = voucherTypeID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}
