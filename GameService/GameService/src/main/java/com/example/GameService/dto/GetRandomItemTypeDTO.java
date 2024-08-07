package com.example.GameService.dto;

import lombok.Data;

@Data
public class GetRandomItemTypeDTO {
    public Long userID;
    public Long eventID;
    public Long gameID;

    public Long getUserID() {
        return userID;
    }

    public Long getEventID() {
        return eventID;
    }

    public Long getGameID() {
        return gameID;
    }
}
