package com.NotficationService.mapper;

import com.NotficationService.dto.request.SubscribeTopicRequest;
import com.NotficationService.entity.UserSubscribe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserSubscribeMapper {

    @Mapping(target = "subscribeList", expression = "java(mapToSubscribeList(request))")
    UserSubscribe toUserSubscribe(SubscribeTopicRequest request);

    default List<UserSubscribe.SubscribeItem> mapToSubscribeList(SubscribeTopicRequest request) {
        List<UserSubscribe.SubscribeItem> list = new ArrayList<>();
        UserSubscribe.SubscribeItem subscribeList = new UserSubscribe.SubscribeItem();
        subscribeList.setTopic(request.getTopic());
        subscribeList.setTypeSubscribe(request.getTypeSubscribe());
        list.add(subscribeList);
        return list;
    }
}
