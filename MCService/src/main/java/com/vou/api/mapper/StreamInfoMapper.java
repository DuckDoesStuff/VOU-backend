package com.vou.api.mapper;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.dto.stream.StreamInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StreamInfoMapper {

    @Mapping(source = "gameID", target = "streamKey") // Nếu gameID ánh xạ đến eventID trong StreamInfo
    StreamInfo infoForStreamToStreamInfo(InfoForStream infoForStream);
}
