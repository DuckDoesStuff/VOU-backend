package com.vou.api.dto.response;

import com.vou.api.entity.UserScore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class GameScore {
    String gameID;
    String eventID;
    List<UserScore> userScores;
}
