package com.vou.api.socket.manage;

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

    //topic,List<socketID>
    final Map<String, List<String>> rooms = new HashMap<>();
    //user, List<topics>
    final Map<String, List<String>> users = new HashMap<>();


    public void addNewUser(String clientID, List<String> topics){
        users.put(clientID, topics);
    }

    public void addNewUserToRoom(String topic, String clientID) {
        rooms.get(topic).add(clientID);
    }

    public void removeUser(String clientID) {
        users.remove(clientID);
    }

    public void initRoom(String roomID) {
        rooms.put(roomID, new ArrayList<>());
    }

    public void cleanRoom(String roomID) {
        rooms.remove(roomID);
    }

}
