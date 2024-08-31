package com.vou.api.socket.manage;

import com.vou.api.entity.UserInfo;
import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Component
public class SocketInfoManager {
    // socketID,userInfo
    final Map<String, UserInfo> userIDs = new HashMap<>();
    // room,List<socketID>
    final Map<String, List<String>> roomIDs = new HashMap<>();

    public void addNewUser(String roomID, String clientID, UserInfo userInfo) {
        userIDs.put(clientID, userInfo);
        roomIDs.get(roomID).add(clientID);
    }

    public void removeUser(String roomID, String clientID) {
        roomIDs.get(roomID).remove(clientID);
        userIDs.remove(clientID);
    }

    public UserInfo getUserInfo(String clientID) {
        return userIDs.get(clientID);
    }

    public void initRoom(String roomID) {
        roomIDs.put(roomID, new ArrayList<>());
    }

    public void cleanRoom(String roomID) {
        roomIDs.remove(roomID);
    }

}
