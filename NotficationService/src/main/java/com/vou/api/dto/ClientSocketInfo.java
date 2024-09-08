package com.vou.api.dto;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientSocketInfo {
    String userID;
    SocketIOClient socketIOClient;
}
