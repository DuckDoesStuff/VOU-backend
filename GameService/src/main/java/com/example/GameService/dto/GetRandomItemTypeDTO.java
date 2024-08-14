package com.example.GameService.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class GetRandomItemTypeDTO {
    public Long userID;
    public Long eventID;
    public ObjectId gameID;
}
