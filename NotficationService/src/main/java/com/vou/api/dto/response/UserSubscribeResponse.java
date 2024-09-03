package com.vou.api.dto.response;

import com.vou.api.entity.UserSubscribe;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserSubscribeResponse {
    List<UserSubscribe.SubscribeItem> subscribeList;
}
