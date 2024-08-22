package MCService.dto.response;

import MCService.entity.UserScore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class GameScore {
    String gameID;
    String eventID;
    List<UserScore> userScores;
}
