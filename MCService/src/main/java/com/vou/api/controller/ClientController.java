package com.vou.api.controller;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.dto.response.OnlineStreams;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.service.StreamService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.vou.api.dto.ApiResponse;
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
    @PostMapping("/testStartStream")
    public ApiResponse<String> testStartStream(@RequestBody InfoForStream infoForStream) {
        ((StreamService)streamService).startStream(infoForStream);
        return ApiResponse.<String>builder()
                .result("OK")
                .build();

    }
}
