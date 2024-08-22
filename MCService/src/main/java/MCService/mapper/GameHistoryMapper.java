package MCService.mapper;

import MCService.entity.GameHistory;
import MCService.entity.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameHistoryMapper {
    GameHistory userInfoToGameHistory(UserInfo userInfo);
}
