package com.vou.api.dto.request;

import com.vou.api.entity.Game.Question;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoForStream {
    String gameID;
    String gameName;
    String eventID;
    String eventName;
    String eventBanner;
    List<Question> questions;
    String[] videoUrl;
}
