package MCService.controller;

import MCService.dto.response.OnlineStreams;
import MCService.dto.stream.StreamInfo;
import MCService.service.StreamService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import MCService.dto.ApiResponse;
import java.beans.PropertyChangeListener;

@RestController
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE)
@RequestMapping("/client")
public class ClientController {
    @Autowired
    PropertyChangeListener streamService;
    @GetMapping("/getOnlineStreamInfo")
    public ApiResponse<StreamInfo>getDetailsOfStream(@RequestParam String streamKey) {
        return ApiResponse.<StreamInfo>builder()
                .result(((StreamService)streamService).getStreamInfo(streamKey))
                .build();
    }

    @GetMapping("/getOnlineStreams")
    public ApiResponse<OnlineStreams>getOnlineStreams() {
        return ApiResponse.<OnlineStreams>builder()
                .result(((StreamService)streamService).getStreamKeys())
                .build();
    }
}
