package MCService.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameHistory {
    String userID;
    String gameID;
    String eventID;
    LocalDateTime joinTime;
    LocalDateTime leaveTime;
}

