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
    final Map<String, UserInfo> users = new HashMap<>();
    // room,List<socketID>
    final Map<String, List<String>> rooms = new HashMap<>();

    public void addNewUser(String room, String clientID, UserInfo userInfo) {
        users.put(clientID, userInfo);
        rooms.get(room).add(clientID);
    }

    public void removeUser(String room, String clientID) {
        rooms.get(room).remove(clientID);
        users.remove(clientID);
    }

    public UserInfo getUserInfo(String clientID) {
        return users.get(clientID);
    }

    public void initRoom(String room) {
        rooms.put(room, new ArrayList<>());
    }

    public void cleanRoom(String room) {
        rooms.remove(room);
    }

}
