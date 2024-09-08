package com.vou.api.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class JoinRoomReqest {
    String userID;
    List<String> topics;
}
