package MCService.mapper;

import MCService.dto.request.InfoForStream;
import MCService.dto.stream.StreamInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StreamInfoMapper {

    @Mapping(source = "gameID", target = "streamKey") // Nếu gameID ánh xạ đến eventID trong StreamInfo
    StreamInfo infoForStreamToStreamInfo(InfoForStream infoForStream);
}
