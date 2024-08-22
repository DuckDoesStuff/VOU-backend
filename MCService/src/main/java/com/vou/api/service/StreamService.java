package com.vou.api.service;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.dto.response.GameScore;
import com.vou.api.dto.response.OnlineStreams;
import com.vou.api.dto.socket.ServerSocketInformation;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.entity.UserInfo;
import com.vou.api.entity.UserScore;
import com.vou.api.mapper.StreamInfoMapper;
import com.vou.api.socket.manage.SocketHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//@Scheduled()
@Service
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StreamService implements PropertyChangeListener {
    FFmpegService ffmpegService;
    SocketHandler socketHandler;
    StreamInfoMapper streamInforMapper;
    Map<String, StreamInfo> streams = new HashMap<>();
    KafkaTemplate<String, Object> kafkaTemplate;

    int numberTopUser = 10;

    public void startStream(InfoForStream infoForStream) {
        StreamInfo streamInfo = streamInforMapper.infoForStreamToStreamInfo(infoForStream);
        ServerSocketInformation.getHistory().put(streamInfo.getStreamKey(),new ArrayList<>());
        streamInfo.addPropertyChangeListener(this);
        streams.put(streamInfo.getStreamKey(), streamInfo);
        try {
            ffmpegService.streamVideo(streamInfo);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Async
    // Hàm xử lý
    public void propertyChange(PropertyChangeEvent evt) {
        String streamKey = evt.getPropertyName();
        StreamInfo streamInfo = streams.get(streamKey);
        int order = streamInfo .getOrder();
        if (order == 0) {
            //Video Gioi Thieu
            return;
        }
        if (order == -2) {
            //endstream
            endStream(streamKey);
            return;
        }
        if (order%2 != 0 ) {
            socketHandler.sendRoomMessage(streamKey,"Question",streamInfo.getQuestions().get(order-1));
        } else {
            socketHandler.sendRoomMessage(streamKey,"Answer",streamInfo.getQuestions().get(order-1).getAnswer());
        }
    }

    public void endStream(String streamKey) {
        StreamInfo streamInfo = streams.get(streamKey);
        //disconnect users
        socketHandler.disconnectRoom(streamKey);
        //save participants history
        List<UserInfo> streamHistory = ServerSocketInformation.getHistory().get(streamKey);
        kafkaTemplate.send("SaveGameHistory", streamHistory);

        //Process UserScore
        List<UserScore> sortedUserScoreList =  ServerSocketInformation.getUserScore().get(streamKey).entrySet()
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
        kafkaTemplate.send("SaveGameScore", GameScore.builder()
                .gameID(streamKey)
                .eventID(streamInfo.getEventID())
                .userScores(sortedUserScoreList.subList(0, maxIndex)));

        //Clean Resource
        cleanResouce(streamKey);

    }

    public StreamInfo getStreamInfo(String streamKey) {
        return streams.get(streamKey);
    }

    public OnlineStreams getStreamKeys() {
        Set<String> rooms = streams.keySet();

        return OnlineStreams.builder()
                .rooms(rooms)
                .build();
    }

    void cleanResouce(String streamKey) {
        streams.remove(streamKey);
        socketHandler.cleanRoom(streamKey);
    }

//    @PostConstruct
    //Test
//    public void init() {
//        String[] ListVideo = {
//                "https://firebasestorage.googleapis.com/v0/b/vou-hcmus-69ff6.appspot.com/o/videoplayback%20(1).mp4?alt=media&token=c267ef95-8c87-42e5-ae8d-66c150ecc03a&fbclid=IwY2xjawEueLtleHRuA2FlbQIxMAABHTQJfMLDFouRorbTOYNzLImmPInBLXQW_lPEqLYqSo21Bx40mvNb94_h_A_aem_OlKFd-Vmyj_Z59Mi918_QA",
//                "https://firebasestorage.googleapis.com/v0/b/vou-hcmus-69ff6.appspot.com/o/videoplayback%20(2).mp4?alt=media&token=99d6c801-a98a-44d7-9371-05031031f55a&fbclid=IwY2xjawEueNtleHRuA2FlbQIxMAABHTIqxt8gWG2_xJEMkwTvyLLHxZI_bysjzPI4UHDy5RWuD4vpMuD5XyfQjg_aem_xiZTH9llhBopc9e5V0uL1g",
//        };
////                "C:\\Users\\Toan\\Desktop\\WorkStation\\5742343248928.mp4",
//
////                "C:\\Users\\Toan\\Desktop\\WorkStation\\5742341682520.mp4",
////                "C:\\Users\\Toan\\Desktop\\WorkStation\\5742505679730.mp4"};
//        try {
//            ffmpegService.streamVideo(ListVideo, "stream");
//        } catch (Exception e) {
//            System.out.println("hhhhh");
//            System.out.println(e);
//            e.printStackTrace();
//        }
//    }

}
