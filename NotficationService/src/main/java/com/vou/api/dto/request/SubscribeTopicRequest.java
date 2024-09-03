package com.vou.api.dto.request;


import com.vou.api.entity.UserSubscribe;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscribeTopicRequest {
    @NonNull
    String userID;
    List<UserSubscribe.SubscribeItem> subscribeList;
}