package com.vou.api.socket.manage;

import com.corundumstudio.socketio.SocketIOServer;
import com.vou.api.dto.SocketResponse;
import com.vou.api.socket.listener.GeneralListener;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocketHandler {
    SocketIOServer server;
    GeneralListener handler;
//    SocketInfoManager socketInfoManager;
    public SocketHandler(SocketIOServer server, GeneralListener handler, SocketInfoManager socketInfoManager) {
        this.server = server;
        this.handler = handler;
//        this.socketInfoManager = socketInfoManager;
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


    public void sendRoomMessage(String room , String eventName , Object message) {
        server.getRoomOperations(room).sendEvent(eventName,message);
    }

    public void sendRomeByteMessage1(String room ,String eventName ,byte[] message) {
        server.getRoomOperations(room).sendEvent(eventName,message);
    }


    public void disconnectRoom(String room) {
        server.getRoomOperations(room).sendEvent("disconnectStream", SocketResponse.<String>builder()
                .code(-2)
                .message("Stream ended")
                .build());
        server.getRoomOperations(room).disconnect();
    }
//    public void cleanRoom(String room) {
//        socketInfoManager.cleanRoom(room);
//    }
//
//    public void initRoom(String room) {
//        socketInfoManager.initRoom(room);
//    }
}
