package com.vou.api.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.UUID;

@Data
public class GetRandomItemTypeDTO {
    public String userID;
    public Long eventID;
    public ObjectId gameID;
}
