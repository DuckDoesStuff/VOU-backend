package com.vou.api.service;

import com.vou.api.dto.request.UserAnswer;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.entity.UserInfo;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StreamInfoManager {
    final Map<String, StreamInfo> listStream = new HashMap<>();
    //room,List<UserInfo>
    final Map<String, List<UserInfo>> listHistory = new HashMap<>();
    //room, <userID, score>
    final Map<String,Map<String,Integer>> listUserScore = new HashMap<>();

    public StreamInfo getStreamInfo(String streamKey) {
        return listStream.get(streamKey);
    }
    public Set<String> getStreamKeys() {
        return listStream.keySet();
    }

    //Get history of a stream
    public List<UserInfo> getHistoryOfStream(String streamKey) {
        return listHistory.get(streamKey);
    }

    //Add a new user history to a stream's history
    public void addNewUserHistory(String streamKey, UserInfo userInfo) {
        if (listHistory.containsKey(streamKey)) {
            listHistory.get(streamKey).add(userInfo);
        }
    }

    //Get list(map) user score of a stream
    public Map<String,Integer> getListUserScoreOfStream(String streamKey) {
        return listUserScore.get(streamKey);
    }

    //Update score for user
    public void updateUserScoreOfStream(UserAnswer userAnswer) {
        StreamInfo streamInfo = this.getStreamInfo(userAnswer.getRoomID());
        int questionId = userAnswer.getQuestionId();

        if (userAnswer.getAnswer() ==  streamInfo.getQuestions().get(questionId).getCorrectAnswer()) {
            listUserScore.get(userAnswer.getRoomID()).merge(userAnswer.getUserID(),1,Integer::sum);
        }
    }

    public void initStream(StreamInfo streamInfo) {
        listStream.put(streamInfo.getStreamKey(), streamInfo);
        listHistory.put(streamInfo.getStreamKey(),new ArrayList<>());
        listUserScore.put(streamInfo.getStreamKey(),new HashMap<>());
    }

    public void cleanStream(String streamKey) {
        listStream.remove(streamKey);
        listHistory.remove(streamKey);
        listUserScore.remove(streamKey);
    }

}
