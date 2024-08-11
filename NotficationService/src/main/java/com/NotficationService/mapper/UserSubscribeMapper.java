package com.NotficationService.mapper;

import com.NotficationService.dto.request.SubscribeTopicRequest;
import com.NotficationService.entity.UserSubscribe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserSubscribeMapper {
    UserSubscribe toUserSubscribe(SubscribeTopicRequest request);
}
