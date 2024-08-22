package MCService.dto.socket;

import MCService.entity.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

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
    //userID, score
    @Getter
    static final Map<String,Integer> userScore = new HashMap<>();
}
