package com.vou.api.mapper;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.dto.response.Stream2User;
import com.vou.api.dto.stream.StreamInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StreamInfoMapper {

    @Mapping(source = "gameID", target = "roomID") // Nếu gameID ánh xạ đến eventID trong StreamInfo
    StreamInfo infoForStreamToStreamInfoVideo(InfoForStream infoForStream);

    @Mappings({
        @Mapping(source = "eventBanner", target = "banner")
    })
    Stream2User streamInfoToStreamList(StreamInfo streamInfo);
}
