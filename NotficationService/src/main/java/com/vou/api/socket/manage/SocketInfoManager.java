package com.vou.api.socket.manage;

import com.corundumstudio.socketio.SocketIOClient;
import com.vou.api.dto.ClientSocketInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Getter
@Component
public class SocketInfoManager {

    //userID, socketClient
    final Map<String,String> users = new HashMap<>();
    //socketClient, clientSocketInfo
    final Map<String, ClientSocketInfo> sockets = new HashMap<>();

    public void addNewUser(String userID, String clientSocketID, SocketIOClient socketIOClient){
        users.put(userID, clientSocketID);

        sockets.put(clientSocketID, ClientSocketInfo.builder()
                        .userID(userID)
                        .socketIOClient(socketIOClient)
                        .build());
    }


    public void removeUser(String clientSocketID) {
        ClientSocketInfo clientSocketInfo = sockets.get(clientSocketID);
        users.remove(clientSocketInfo.getUserID());
        sockets.remove(clientSocketID);
    }


}
