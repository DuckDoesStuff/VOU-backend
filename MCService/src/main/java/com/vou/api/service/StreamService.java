package com.vou.api.service;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.dto.response.GameScore;
import com.vou.api.dto.response.Stream2User;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.entity.UserInfo;
import com.vou.api.entity.UserScore;
import com.vou.api.mapper.StreamInfoMapper;
import com.vou.api.socket.manage.SocketHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.stream.Collectors;

//@Scheduled()
@Service
//@AllArgsConstructor
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class StreamService implements PropertyChangeListener {
    final FFmpegService ffmpegService;
    final SocketStreamService socketStreamService;
    final SocketHandler socketHandler;
    final StreamInfoMapper streamInforMapper;
    final KafkaTemplate<String, Object> kafkaTemplate;
    final StreamInfoManager streamInfoManager;
    private final StreamInfoMapper streamInfoMapper;

    int numberTopUser = 10;

    public void startStream(InfoForStream infoForStream) {
        StreamInfo streamInfo = streamInforMapper.infoForStreamToStreamInfoVideo(infoForStream);
        //Khoi tao stream va socket
//        log.info(streamInfo.toString());
        socketHandler.initRoom(streamInfo.getRoomID());
        streamInfoManager.initStream(streamInfo);
        //Them Listener
        streamInfo.addPropertyChangeListener(this);
        try {
//            socketStreamService.streamVideoData(streamInfo);
            socketStreamService.streamText(streamInfo);
//            socketStreamService.streamVideoDataJcodec(streamInfo);
//            ffmpegService.streamVideo(streamInfo);
//            ffmpegService.streamVideo2(streamInfo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
//    @Async
    // Hàm xử lý
    public void propertyChange(PropertyChangeEvent evt) {
        String roomID = evt.getPropertyName();
        StreamInfo streamInfo = streamInfoManager.getStreamInfo(roomID);
        int order = streamInfo.getOrder();
        if (order == 0) {
            log.info("intro");
            //Video Gioi Thieu
            return;
        }
        if (order == -1) {
            log.info("startStream");
            return;
        }
        if (order == -2) {
            log.info("endStream");
            //endstream
            endStream(roomID);
            return;
        }
        if (order%2 != 0 ) {
//            log.info(streamInfo.getQuestions().get(order-1).toString());
            socketHandler.sendRoomStringMessage(roomID,"Question", streamInfo.getQuestions().get(order-1));
        } else {
//            log.info(streamInfo.getQuestions().get(order-1).toString());
            socketHandler.sendRoomStringMessage(roomID,"Answer", streamInfo.getQuestions().get(order-1).getAnswers());
        }
    }

    public void endStream(String roomID) {
        StreamInfo streamInfo = streamInfoManager.getStreamInfo(roomID);
        streamInfo.removePropertyChangeListener(this);
        //disconnect users
        socketHandler.disconnectRoom(roomID);
        //save participants history
        List<UserInfo> streamHistory = streamInfoManager.getHistoryOfStream(roomID);
        kafkaTemplate.send("SaveGameHistory", streamHistory);

        //Process UserScore
        List<UserScore> sortedUserScoreList = streamInfoManager.getListUserScoreOfStream(roomID)
                .entrySet()
                .stream()
                .map(entry -> UserScore.builder()
                        .userID(entry.getKey())
                        .score(entry.getValue())
                        .build())
                        .sorted((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore()))
                .collect(Collectors.toList());

        //Calculate Top User
        int maxIndex = numberTopUser;
        while (maxIndex < sortedUserScoreList.size() &&
                sortedUserScoreList.get(maxIndex).getScore() == sortedUserScoreList.get(maxIndex - 1).getScore()) {
            maxIndex++;
        }
        maxIndex = Math.min(maxIndex, sortedUserScoreList.size());
        List<UserScore> topUserScores = sortedUserScoreList.subList(0, maxIndex);
        //Save UserScore
        kafkaTemplate.send("SaveGameScore", GameScore.builder()
                .gameID(roomID)
                .eventID(streamInfo.getEventID())
                .userScores(topUserScores)
                .build());

        //Clean Resource
        cleanResouce(roomID);
    }

    public StreamInfo getStreamInfo(String streamKey) {
        return streamInfoManager.getStreamInfo(streamKey);
    }

    public List<Stream2User> getStreams() {
        List<StreamInfo> streams = streamInfoManager.getStreams();
        List<Stream2User> stream2UsersList = new ArrayList<>();
        for (StreamInfo streamInfo : streams) {
            Stream2User stream2User = streamInfoMapper.streamInfoToStreamList(streamInfo);
            stream2UsersList.add(stream2User);
        }
        return stream2UsersList;
    }

    void cleanResouce(String streamKey) {
        streamInfoManager.cleanStream(streamKey);
        socketHandler.cleanRoom(streamKey);
    }
}
