package com.NotficationService.dto.response;

import com.NotficationService.entity.UserSubscribe;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserSubscribeResponse {
    List<UserSubscribe.SubscribeItem> subscribeList;
}
