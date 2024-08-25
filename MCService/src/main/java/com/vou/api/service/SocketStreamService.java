package com.vou.api.service;

import com.vou.api.dto.stream.StreamEvent;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.socket.manage.SocketHandler;
import com.vou.api.utils.FileUtils;
import com.vou.api.utils.VideoUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.jcodec.api.*;

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
    public void streamVideoDataJcodec(StreamInfo streamInfo) throws MalformedURLException {
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
        List<String> fileNames = new ArrayList<>();
        for (int i = 0; i < videoStream.size(); i++) {
            try {
                URL url = new URL(listVideo[i]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                String newFileName = streamInfo.getStreamKey() + "_" + i + ".mp4";
                FileUtils.convertInputStreamToFile(connection.getInputStream(),newFileName);
                fileNames.add(newFileName);
            }  catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < videoStream.size(); i++) {
            try {
//                connection.getInputStream();
                //Xử lý dử liệu gửi socket
                Iterator<Picture> frames = VideoUtils.getFrames(fileNames.get(i));
                while (frames.hasNext()) {
                    Picture picture = frames.next();
                    byte[] frameData = VideoUtils.pictureToByteArray(picture);
                    log.info(""+frameData.length);
                    socketHandler.sendRomeByteMessage(streamKey, "stream", frameData);
                    // Có thể thêm một số cơ chế để điều khiển tốc độ truyền tải khung hình
                    int frameInterval = 1000 / 25;
                    Thread.sleep(frameInterval); // Giả sử 30 fps, 33 ms giữa các khung hình
                }
            } catch (IOException | JCodecException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
