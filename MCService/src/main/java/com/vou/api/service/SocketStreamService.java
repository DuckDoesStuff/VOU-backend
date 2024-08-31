package com.vou.api.service;

import com.vou.api.dto.SocketResponse;
import com.vou.api.dto.response.Answer2User;
import com.vou.api.dto.response.Question2User;
import com.vou.api.dto.stream.Question;
import com.vou.api.dto.stream.Script;
import com.vou.api.dto.stream.StreamEvent;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.mapper.QuestionMapper;
import com.vou.api.socket.manage.SocketHandler;
import com.vou.api.utils.FileUtils;
import com.vou.api.utils.VideoUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor()
@Slf4j
public class SocketStreamService {
    final SocketHandler socketHandler;
    final FFmpegService ffmpegService;
    final QuestionMapper questionMapper;
    long defaultTime = 6;
    double wordPerSecond = 2.5;
    @Value("${stream.default.intro}")
    String defaultIntro;

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

                    try (InputStream videoInput = connection.getInputStream();
                    //try (InputStream videoInput = new FileInputStream(listVideo[i])) {
                        ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
                        long bitrate = allStream.get(i).bit_rate; // Số byte mỗi giây
                        int fps = (int) FFmpegService.getFPS(videoStream.get(i));
//                        if (fps == -1) fps = 25; // giả sử fps = 30 nếu không tính được fps
                        int frameInterval = 1000 / fps; // Thời gian nghỉ giữa các khung hình (ms)
                        int resolution = (int) (bitrate / 8 / fps);
                        byte[] buffer = new byte[resolution];
                        int bytesRead;

                        log.info("" + fps + " " + bitrate + " " + resolution);
                        while ((bytesRead = videoInput.read(buffer)) != -1) {
                            bufferedOutputStream.write(buffer, 0, bytesRead);
                            bufferedOutputStream.flush();
                            socketHandler.sendRomeByteMessage1(streamKey, "stream", buffer);
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
//        List<FFmpegStream> videoStream = new ArrayList<>();
//        for (String videoUrl : listVideo) {
//            videoStream.add(FFmpegService.getOnlyVideoFFmpegStream(videoUrl));
//        }
//
//        List<FFmpegFormat> allStream = new ArrayList<>();
//        for (String videoUrl : listVideo) {
//            allStream.add(FFmpegService.getVideoFFmpegFormat(videoUrl));
//        }

        streamInfo.setOrder(-1);
        streamInfo.setEvent(StreamEvent.START_STREAM);
        streamInfo.raiseEvent(streamInfo.getStreamKey());
        List<String> fileNames = new ArrayList<>();
        for (int i = 0; i < listVideo.length; i++) {
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
        for (int i = 0; i < fileNames.size(); i++) {
            try {
//                connection.getInputStream();
                //Xử lý dử liệu gửi socket

                SeekableByteChannel byteChannel = NIOUtils.readableChannel(new java.io.File(fileNames.get(i)));
                FrameGrab grab = FrameGrab.createFrameGrab(byteChannel);
                Picture picture;
                while ((picture = grab.getNativeFrame()) != null) {
                    // Convert Picture to BufferedImage (if needed)
                    // Here you can process the BufferedImage as needed
                    byte[] frameData = VideoUtils.pictureToByteArray(picture);
                    log.info(""+frameData.length);
                    socketHandler.sendRomeByteMessage(streamKey, "stream", frameData);
                    int frameInterval = 1000 / 25;
                    Thread.sleep(frameInterval); // Giả sử 30 fps, 33 ms giữa các khung hình
                }
//                Iterator<Picture> frames = VideoUtils.getFrames(fileNames.get(i));
//                while (frames.hasNext()) {
//                    Picture picture = frames.next();
//                    byte[] frameData = VideoUtils.pictureToByteArray(picture);
//                    log.info(""+frameData.length);
//                    socketHandler.sendRomeByteMessage(streamKey, "stream", frameData);
//                    // Có thể thêm một số cơ chế để điều khiển tốc độ truyền tải khung hình
//                    int frameInterval = 1000 / 25;
//                    Thread.sleep(frameInterval); // Giả sử 30 fps, 33 ms giữa các khung hình
//                }
            } catch (IOException | JCodecException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void streamText(StreamInfo streamInfo) throws InterruptedException {
        streamInfo.setOrder(-1);
        streamInfo.setEvent(StreamEvent.START_STREAM);

        String streamKey = streamInfo.getStreamKey();
//        Script script = streamInfo.getScript();

        List<Question> questionList = streamInfo.getQuestions();

        streamInfo.setOrder(0);
        streamInfo.raiseEvent(streamInfo.getStreamKey());
        socketHandler.sendRomeByteMessage(streamKey, "stream", SocketResponse.<String>builder()
                .code(0)
                .result(defaultIntro));
        log.info(defaultIntro);
        Thread.sleep((FileUtils.calculateTTSDuration_Second(defaultIntro, wordPerSecond)+ defaultTime)*1000);

        for (int i = 0; i < questionList.size(); i++) {
            // Thứ tự câu hỏi
            streamInfo.setOrder(i+1);
            streamInfo.setEvent(StreamEvent.QUESTION);
            streamInfo.raiseEvent(streamInfo.getStreamKey());

            Question2User question2User = questionMapper.questionToQuestion2User(questionList.get(i));
            socketHandler.sendRomeByteMessage(streamKey, "stream",SocketResponse.<Question2User>builder()
                    .code(1)
                    .result(question2User));

            log.info(question2User.toString());
            Thread.sleep((FileUtils.calculateTTSDuration_Second(questionList.get(i).getQuestion(), wordPerSecond)+ defaultTime)*1000);
            // Dod cau tra loi
            streamInfo.setEvent(StreamEvent.ANSWER);
            streamInfo.raiseEvent(streamInfo.getStreamKey());

            Answer2User answer2User = questionMapper.questionToAnswer2User(questionList.get(i));
            socketHandler.sendRomeByteMessage(streamKey, "stream", SocketResponse.<Answer2User>builder()
                    .code(2)
                    .result(answer2User));

            log.info(answer2User.toString());
            Thread.sleep((FileUtils.calculateTTSDuration_Second(questionList.get(i).getAnswers().toString(), wordPerSecond)+ defaultTime)*1000);
        }
        // end
        streamInfo.setOrder(-2);
        streamInfo.setEvent(StreamEvent.STOP_STREAM);
        // wait for receiving user answer
        Thread.sleep(defaultTime);
        //actually endstream
        streamInfo.raiseEvent(streamInfo.getStreamKey());

    }


}
