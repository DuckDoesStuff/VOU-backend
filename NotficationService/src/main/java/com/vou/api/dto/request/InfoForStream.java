package com.vou.api.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
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
