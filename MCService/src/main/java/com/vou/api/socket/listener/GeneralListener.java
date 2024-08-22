package com.vou.api.socket.listener;

import com.vou.api.dto.socket.ServerSocketInformation;
import com.vou.api.dto.request.UserAnswer;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.entity.UserInfo;
import com.vou.api.service.StreamService;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneralListener {

    @Autowired
    SocketIOServer server;
    @Autowired
    PropertyChangeListener streamService;
    //user room
    static final Map<String, UserInfo> users = ServerSocketInformation.getUsers();
    static final Map<String, List<String>> rooms = ServerSocketInformation.getRooms();
    static final Map<String, List<UserInfo>> history = ServerSocketInformation.getHistory();
    static final Map<String,Map<String,Integer>> userScore = ServerSocketInformation.getUserScore();
    @Value("${rtmp.maxConnects}")
    int MAX_CONNECTS;

    @OnConnect
    public void onConnect(SocketIOClient client) {
//        System.out.println("Client connected: " + client.getSessionId());
        String clientId = client.getSessionId().toString();
        users.put(clientId, null);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
        UserInfo userInfo = users.get(clientId);
        userInfo.setLeaveTime(LocalDateTime.now());
        if (!Objects.isNull(userInfo)) {
//            System.out.println(String.format("Client disconnected: %s from : %s", clientId, userInfo.getRoom()));
            history.get(userInfo.getGameID()).add(userInfo);
            rooms.get(userInfo.getGameID()).remove(clientId);
            users.remove(clientId);
        }
    }

    @OnEvent("joinStream")
    public void onJoinRoom(SocketIOClient client,  String userID, String eventID, String room, AckRequest ackRequest) {
        int connectedClients = server.getRoomOperations(room).getClients().size();
        if (connectedClients == MAX_CONNECTS) {
            ackRequest.sendAckData("full");
            log.info("Stream is full: " + room);
            return;
        }
        client.joinRoom(room);
        // Lưu thông tin user
        UserInfo userInfo = UserInfo.builder()
                .userID(userID)
                .gameID(room)
                .eventID(eventID)
                .joinTime(LocalDateTime.now())
                .build();
        users.put(client.getSessionId().toString(),userInfo);
        rooms.get(room).add(client.getSessionId().toString());
    }

    @OnEvent("Answer")
    public void onAnswer(SocketIOClient client, UserAnswer userAnswer) {
        String streamKey = users.get(client.getSessionId().toString()).getGameID();
        StreamInfo streamInfo = ((StreamService)streamService).getStreamInfo(streamKey);
        int questionId = userAnswer.getQuestionId();

        if (userAnswer.getAnswer() ==  streamInfo.getQuestions().get(questionId).getCorrectAnswer()) {
            userScore.get(userAnswer.getRoomID()).merge(userAnswer.getUserID(), 1, Integer::sum);
        }
    }

}
