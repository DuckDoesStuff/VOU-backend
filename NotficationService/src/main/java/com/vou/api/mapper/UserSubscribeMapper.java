package com.vou.api.mapper;

import com.vou.api.dto.request.SubscribeTopicRequest;
import com.vou.api.entity.UserSubscribe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSubscribeMapper {
    UserSubscribe toUserSubscribe(SubscribeTopicRequest request);
}
