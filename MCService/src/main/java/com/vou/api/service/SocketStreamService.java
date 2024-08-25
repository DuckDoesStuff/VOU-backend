package com.vou.api.service;

import com.vou.api.dto.stream.StreamEvent;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.socket.manage.SocketHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Slf4j
public class SocketStreamService {
    SocketHandler socketHandler;
    FFmpegService ffmpegService;

    public void streamVideoData(StreamInfo streamInfo) {
        String[] listVideo = streamInfo.getVideoUrl();
        String streamKey = streamInfo.getStreamKey();
        if (listVideo == null || listVideo.length == 0) {
            return;
        }
        List<FFmpegStream> videoStream = new ArrayList<>();
        for (String videoUrl : listVideo) {
            videoStream.add(FFmpegService.getOnlyVideoFFmpegStream(videoUrl));
        }

        List<FFmpegFormat> allStream = new ArrayList<>();
        for (String videoUrl : listVideo) {
            allStream.add(FFmpegService.getVideoFFmpegFormat(videoUrl));
        }

        streamInfo.setOrder(-1);
        streamInfo.setEvent(StreamEvent.START_STREAM);
        streamInfo.raiseEvent(streamInfo.getStreamKey());

        for (int i = 0; i < videoStream.size(); i++) {
            try {
                URL url = new URL(listVideo[i]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                    try (InputStream videoInput = connection.getInputStream()) {
                    //try (InputStream videoInput = new FileInputStream(listVideo[i])) {

                        long bitrate = allStream.get(i).bit_rate; // Số byte mỗi giây

                        int fps = (int) FFmpegService.getFPS(videoStream.get(i));
//                        if (fps == -1) fps = 25; // giả sử fps = 30 nếu không tính được fps
                        int frameInterval = 1000 / fps; // Thời gian nghỉ giữa các khung hình (ms)
                        int resolution = (int) (bitrate / 8 / fps);
                        byte[] buffer = new byte[resolution];
                        int bytesRead;

                        log.info("" + fps + " " + bitrate + " " + resolution);
                        while ((bytesRead = videoInput.read(buffer)) != -1) {
                            log.info("Byteread: " + bytesRead);
                            socketHandler.sendRomeByteMessage(streamKey, "stream", Arrays.copyOf(buffer, bytesRead));
                            ;
                            // Thêm thời gian nghỉ để đảm bảo stream theo thời gian thực
                            Thread.sleep(frameInterval);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
