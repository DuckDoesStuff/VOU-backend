package com.vou.api.socket.listener;

import com.vou.api.dto.SocketResponse;
import com.vou.api.dto.request.JoinStreamRequest;
import com.vou.api.dto.response.Answer2User;
import com.vou.api.dto.response.Question2User;
import com.vou.api.dto.stream.StreamEvent;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.mapper.QuestionMapper;
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
import java.util.Objects;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    final QuestionMapper questionMapper;
    @Value("${rtmp.maxConnects}")
    int MAX_CONNECTS;
    @Value("${stream.default.intro}")
    String defaultIntro;

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
        String room = joinStreamRequest.getRoomID();
        StreamInfo streamInfo = streamInfoManager.getStreamInfo(room);
        if (streamInfo == null) {
            ackRequest.sendAckData(SocketResponse.builder()
                    .code(-1)
                    .message("Room is not init")
                    .build());
            client.disconnect();
            return;
        }


        log.info("Joining room " + joinStreamRequest.getRoomID());
        try {
            client.joinRoom(joinStreamRequest.getRoomID());
            // Lưu thông tin user
            UserInfo userInfo = UserInfo.builder()
                    .userID(joinStreamRequest.getUserID())
                    .gameID(joinStreamRequest.getRoomID())
                    .eventID(joinStreamRequest.getEventID())
                    .joinTime(LocalDateTime.now())
                    .build();

            socketInfoManager.addNewUser(joinStreamRequest.getRoomID(), client.getSessionId().toString(), userInfo);
            // Giả sử JoinRoomResponse là lớp bạn muốn tạo builder
            int order= streamInfo.getOrder();
            if (streamInfo.getEvent() == StreamEvent.INTRO) {
                ackRequest.sendAckData(SocketResponse.<String>builder()
                        .code(0)
                        .result(defaultIntro)
                        .build());
            } else if (streamInfo.getEvent() == StreamEvent.QUESTION) {
                Question2User question2User = questionMapper.questionToQuestion2User(streamInfo.getQuestions().get(order - 1));
                question2User.setOrder(order);
                ackRequest.sendAckData(SocketResponse.<Question2User>builder()
                        .code(1)
                        .result(question2User)
                        .build());
            } else if (streamInfo.getEvent() == StreamEvent.ANSWER) {
                Answer2User answer2User = questionMapper.questionToAnswer2User(streamInfo.getQuestions().get(order - 1));
                answer2User.setOrder(order);
                ackRequest.sendAckData(SocketResponse.<Answer2User>builder()
                        .code(2)
                        .result(answer2User)
                        .build());
            }
        } catch (Exception e) {
            ackRequest.sendAckData(SocketResponse.builder()
                    .code(-1)
                    .message("Some internal problem")
                    .build());
            client.disconnect();
        }
    }

    @OnEvent("Answer")
    public void onAnswer(SocketIOClient client, UserAnswer userAnswer) {
        streamInfoManager.updateUserScoreOfStream(userAnswer);
    }

}
