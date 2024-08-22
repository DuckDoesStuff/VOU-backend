package com.vou.api.dto.socket;

import com.vou.api.entity.UserInfo;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@FieldDefaults(level= AccessLevel.PRIVATE)
@Data
public class ServerSocketInformation {
    // socketID,userInfo
    @Getter
    static final Map<String, UserInfo> users = new HashMap<>();
    // room,List<socketID>
    @Getter
    static final Map<String, List<String>> rooms = new HashMap<>();
    //room,List<UserInfo>
    @Getter
    static final Map<String, List<UserInfo>> history = new HashMap<>();
    //room, <userID, score>
    @Getter
    static final Map<String,Map<String,Integer>> userScore = new HashMap<>();
}
