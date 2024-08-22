package com.vou.api.socket.manage;

import com.vou.api.dto.socket.ServerSocketInformation;
import com.vou.api.socket.listener.GeneralListener;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocketHandler {
    @Autowired
    SocketIOServer server;
    @Autowired
    GeneralListener handler;
    public SocketHandler(SocketIOServer server, GeneralListener handler) {
        this.server = server;
        this.handler = handler;
        server.addListeners(this.handler);
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        server.start();
    }

    @PreDestroy
    public void stopSocketIOServer() {
        server.stop();
    }

    public void sendRoomMessage(String room ,String eventName ,Object message) {
        server.getRoomOperations(room).sendEvent(eventName,message.toString());
    }
    public void disconnectRoom(String room) {
        server.getRoomOperations(room).sendEvent("disconnect");
        server.getRoomOperations(room).disconnect();
    }

    public void cleanRoom(String room) {
        ServerSocketInformation.getRooms().remove(room);
        ServerSocketInformation.getHistory().remove(room);
        ServerSocketInformation.getUserScore().remove(room);
    }

    public void initRoom(String room) {
        ServerSocketInformation.getRooms().put(room, new ArrayList<>());
        ServerSocketInformation.getHistory().put(room, new ArrayList<>());
        ServerSocketInformation.getUserScore().put(room, new HashMap<>());
    }
}
