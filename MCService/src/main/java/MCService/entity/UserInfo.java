package MCService.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo {
    String userID;
    String gameID;
    String eventID;
    LocalDateTime joinTime;
    LocalDateTime leaveTime;
}
