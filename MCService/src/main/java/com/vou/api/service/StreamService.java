package com.vou.api.service;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.dto.response.OnlineStreams;
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

    int numberTopUser = 10;

    public void startStream(InfoForStream infoForStream) {
        StreamInfo streamInfo = streamInforMapper.infoForStreamToStreamInfoVideo(infoForStream);
        //Khoi tao stream va socket
//        log.info(streamInfo.toString());
        socketHandler.initRoom(streamInfo.getStreamKey());
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
        String streamKey = evt.getPropertyName();
        StreamInfo streamInfo = streamInfoManager.getStreamInfo(streamKey);
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
            endStream(streamKey);
            return;
        }
        if (order%2 != 0 ) {
//            log.info(streamInfo.getQuestions().get(order-1).toString());
            socketHandler.sendRoomMessage(streamKey,"Question", streamInfo.getQuestions().get(order-1));
        } else {
//            log.info(streamInfo.getQuestions().get(order-1).toString());
            socketHandler.sendRoomMessage(streamKey,"Answer", streamInfo.getQuestions().get(order-1).getAnswers());
        }
    }

    public void endStream(String streamKey) {
        StreamInfo streamInfo = streamInfoManager.getStreamInfo(streamKey);
        streamInfo.removePropertyChangeListener(this);
        //disconnect users
        socketHandler.disconnectRoom(streamKey);
        //save participants history
//        List<UserInfo> streamHistory = streamInfoManager.getHistoryOfStream(streamKey);
//        kafkaTemplate.send("SaveGameHistory", streamHistory);

        //Process UserScore
        List<UserScore> sortedUserScoreList = streamInfoManager.getListUserScoreOfStream(streamKey)
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
        List<UserScore> topUserScores = sortedUserScoreList.subList(0, maxIndex);

        //Save UserScore
//        kafkaTemplate.send("SaveGameScore", GameScore.builder()
//                .gameID(streamKey)
//                .eventID(streamInfo.getEventID())
//                .userScores(sortedUserScoreList.subList(0, maxIndex)));

        //Clean Resource
        cleanResouce(streamKey);

    }

    public StreamInfo getStreamInfo(String streamKey) {
        return streamInfoManager.getStreamInfo(streamKey);
    }

    public OnlineStreams getStreamKeys() {
        Set<String> rooms = streamInfoManager.getStreamKeys();

        return OnlineStreams.builder()
                .rooms(rooms)
                .build();
    }

    void cleanResouce(String streamKey) {
        streamInfoManager.cleanStream(streamKey);
        socketHandler.cleanRoom(streamKey);
    }
}
