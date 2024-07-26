package com.example.demo.entity;

import java.io.Serializable;
import java.util.Objects;

public class FavoriteListId implements Serializable {
    private Integer eventID;
    private Integer userID;

    public FavoriteListId() {}

    public FavoriteListId(Integer eventID, Integer userID) {
        this.eventID = eventID;
        this.userID = userID;
    }

    // Getters and Setters
    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    // hashCode and equals methods
    @Override
    public int hashCode() {
        return Objects.hash(eventID, userID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FavoriteListId that = (FavoriteListId) obj;
        return Objects.equals(eventID, that.eventID) && Objects.equals(userID, that.userID);
    }
}
