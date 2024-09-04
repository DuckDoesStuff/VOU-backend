package com.vou.api.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ShareTurnForFriendRequest {
    private String giver;
    private String receiver;
    private ObjectId gameID;
    private Long eventID;
}
