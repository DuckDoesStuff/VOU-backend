package com.vou.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserGamePlaytime {
    private String userID;
    private String gameID;
    private Long totalPlaytime;

    public UserGamePlaytime(String userID, String gameID, Long totalPlaytime) {
        this.userID = userID;
        this.gameID = gameID;
        this.totalPlaytime = totalPlaytime;
    }

}

