package com.vou.api.socket.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.vou.api.dto.SocketResponse;
import com.vou.api.dto.request.JoinRoomReqest;
import com.vou.api.socket.manage.SocketInfoManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor()
public class GeneralListener {

    final SocketIOServer server;
    final SocketInfoManager socketInfoManager;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("onConnect " + client.getSessionId());
//        System.out.println("Client connected: " + client.getSessionId());
//        String clientId = client.getSessionId().toString();
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
//        log.info(clientId + " disconnected");
//        socketInfoManager.removeUser(clientId);
    }

    @OnEvent("joinRooms")
    public void onJoinRooms(SocketIOClient client, JoinRoomReqest joinRoomReqest, AckRequest ackRequest) {
        if (joinRoomReqest == null) {
            ackRequest.sendAckData(SocketResponse.builder()
                    .code(-1)
                    .message("no topic founds")
                    .build());
            client.disconnect();
        }
        try {
            List<String> topics = joinRoomReqest.getTopics();
            for (String topic: topics) {
                client.joinRoom(topic);
            }

            socketInfoManager.addNewUser(joinRoomReqest.getUserID(), client.getSessionId().toString(), client);

            ackRequest.sendAckData(SocketResponse.builder()
                    .code(200)
                    .build());
        } catch (Exception e) {
            ackRequest.sendAckData(SocketResponse.builder()
                    .code(-1)
                    .message("Some internal problem")
                    .build());
            client.disconnect();
        }
    }

    @OnEvent("leaveRoom")
    public void onJoinRooms(SocketIOClient client, String topic) {
        client.leaveRoom(topic);
        socketInfoManager.removeUser(client.getSessionId().toString());
    }
}
