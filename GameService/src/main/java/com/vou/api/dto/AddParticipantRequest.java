package com.vou.api.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class AddParticipantRequest {
    public Long eventID;
    public ObjectId gameID;
    public String userID;
}
