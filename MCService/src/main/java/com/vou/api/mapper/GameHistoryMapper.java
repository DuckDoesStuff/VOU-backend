package com.vou.api.mapper;

import com.vou.api.entity.GameHistory;
import com.vou.api.entity.UserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameHistoryMapper {
    GameHistory userInfoToGameHistory(UserInfo userInfo);
}
