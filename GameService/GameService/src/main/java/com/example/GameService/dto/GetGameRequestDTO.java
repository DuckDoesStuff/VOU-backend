package com.example.GameService.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class GetGameRequestDTO {
    private ObjectId gameID;
    private Long eventID;

}

