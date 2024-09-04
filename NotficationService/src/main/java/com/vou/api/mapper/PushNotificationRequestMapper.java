package com.vou.api.mapper;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.dto.request.PushTopicNotificationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PushNotificationRequestMapper {
    @Mappings({
         @Mapping(target="topic",source = "gameID"),
            @Mapping(target = "title", expression = "java(infoForStream.getGameName() + \" has started\")"),
            @Mapping(target = "content", expression = "java(infoForStream.getGameName() + \" of \" + infoForStream.getEventName() + \" is waiting for you to join\")")
    })
    PushTopicNotificationRequest infoForStreamToPNR(InfoForStream infoForStream);
}
