package com.vou.api.socket.listener;

import com.vou.api.dto.request.JoinStreamRequest;
import com.vou.api.dto.response.JoinRoomResponse;
import com.vou.api.dto.stream.Question;
import com.vou.api.dto.stream.StreamEvent;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.socket.manage.SocketInfoManager;
import com.vou.api.dto.request.UserAnswer;
import com.vou.api.service.StreamInfoManager;
import com.vou.api.entity.UserInfo;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.requests.JoinGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor()
public class GeneralListener {

    final SocketIOServer server;
    final StreamInfoManager streamInfoManager;
    final SocketInfoManager socketInfoManager;
    @Value("${rtmp.maxConnects}")
    int MAX_CONNECTS;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("onConnect " + client.getSessionId());
//        System.out.println("Client connected: " + client.getSessionId());
        String clientId = client.getSessionId().toString();
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
        UserInfo userInfo = socketInfoManager.getUserInfo(clientId);
        if (!Objects.isNull(userInfo)) {
            userInfo.setLeaveTime(LocalDateTime.now());
//            System.out.println(String.format("Client disconnected: %s from : %s", clientId, userInfo.getRoom()));
            //Them lich su
            streamInfoManager.addNewUserHistory(userInfo.getGameID(),userInfo);
            socketInfoManager.removeUser(userInfo.getGameID(),clientId);
        }
    }

    @OnEvent("joinStream")
    public void onJoinRoom(SocketIOClient client, JoinStreamRequest joinStreamRequest, AckRequest ackRequest) {
//        int connectedClients = server.getRoomOperations(joinStreamRequest.getRoom()).getClients().size();
//        if (connectedClients == MAX_CONNECTS) {
//            ackRequest.sendAckData("full");
//            log.info("Stream is full: " + room);
//            return;
//        }
        String room = joinStreamRequest.getRoom();
        StreamInfo stream = streamInfoManager.getStreamInfo(room);
        if (stream == null) {
            ackRequest.sendAckData(JoinRoomResponse.builder()
                    .code(-1)
                    .message("Room is not init")
                    .build());
        }


        log.info("Joining room " + joinStreamRequest.getRoom());
        client.joinRoom(joinStreamRequest.getRoom());
        // Lưu thông tin user
        UserInfo userInfo = UserInfo.builder()
                .userID(joinStreamRequest.getUserID())
                .gameID(joinStreamRequest.getRoom())
                .eventID(joinStreamRequest.getEventID())
                .joinTime(LocalDateTime.now())
                .build();

        socketInfoManager.addNewUser(joinStreamRequest.getRoom(), client.getSessionId().toString(),userInfo);
        // Giả sử JoinRoomResponse là lớp bạn muốn tạo builder
        JoinRoomResponse.JoinRoomResponseBuilder<Question> responseBuilder = JoinRoomResponse.<Question>builder();
        responseBuilder.data(stream.getQuestions().get(stream.getOrder()-1));
        if (stream.getEvent() == StreamEvent.QUESTION) {
            responseBuilder.code(0);
        } else if (stream.getEvent() == StreamEvent.ANSWER) {
            responseBuilder.code(1);
        }
        ackRequest.sendAckData(responseBuilder.build());
    }

    @OnEvent("Answer")
    public void onAnswer(SocketIOClient client, UserAnswer userAnswer) {
        streamInfoManager.updateUserScoreOfStream(userAnswer);
    }

}
