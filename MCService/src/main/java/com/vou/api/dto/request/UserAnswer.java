package com.vou.api.dto.request;

import lombok.Data;

@Data
public class UserAnswer {
    String roomID;
    String userID;
    int questionID;
    int answer;
}
