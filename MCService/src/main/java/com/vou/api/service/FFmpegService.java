package com.vou.api.service;

import com.vou.api.dto.stream.StreamEvent;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.utils.VideoUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FFmpegService {

    static String pathToFFprobe = "ffprobe";
    static String pathToFFmpeg = "ffmpeg";
    @Value("${rtmp.server}")
    String rmtpServer;
    static FFmpeg ffmpeg;
    static FFprobe ffprobe;

    public static FFprobe getFFprobe() throws IOException {
        if (ffprobe == null) {
            ffprobe = new FFprobe(pathToFFprobe);
        }
        return ffprobe;
    }

    public static FFmpeg getFFmpeg() throws IOException {
        if (ffmpeg == null) {
            ffmpeg = new FFmpeg(pathToFFmpeg);
        }
        return ffmpeg;
    }

    public static List<Double> getVideosDuration(String[] videoUrls) {
        List<Double> durations = new ArrayList<>();
        try {
            for (String url : videoUrls) {
                FFprobe ffprobe;
                ffprobe = getFFprobe();
                FFmpegProbeResult probeResult = ffprobe.probe(url);
                double durationSeconds = probeResult.getFormat().duration  * TimeUnit.SECONDS.toNanos(1);
                durations.add(durationSeconds);
            }
            return durations;
        } catch (IOException e) {
            log.info("Khong tim thay log");
            throw new RuntimeException(e);
        }
    }

    public FFmpegFormat getVideoInformation(String videoPath) {
        try {
            FFprobe ffprobe;
            ffprobe = getFFprobe();
            FFmpegProbeResult probeResult = ffprobe.probe(videoPath);
            return probeResult.getFormat();
        } catch (IOException e) {
            log.info("Khong tim thay log");
            throw new RuntimeException(e);
        }
    }

    public void streamVideo(StreamInfo streamInfo) throws IOException {
        String[] listVideo = streamInfo.getVideoUrl();
        String streamKey = streamInfo.getStreamKey();

        if (listVideo == null || listVideo.length == 0) {
            return;
        }
        String outputUrl = rmtpServer + "/" + streamKey;
        FFmpegBuilder builder = new FFmpegBuilder();

        for (String videoPath : listVideo) {
            builder.addInput(videoPath);
        }

        // Xây dựng complex filter
        StringBuilder filterBuilder = new StringBuilder();
        for (int i = 0; i < listVideo.length; i++) {
            filterBuilder.append("[").append(i).append(":v]scale=576:576:force_original_aspect_ratio=decrease,pad=576:576:(ow-iw)/2:(oh-ih)/2[v").append(i).append("];");
        }
        for (int i = 0; i < listVideo.length; i++) {
            filterBuilder.append("[v").append(i).append("][").append(i).append(":a]");
        }
        filterBuilder.append("concat=n=").append(listVideo.length).append(":v=1:a=1[outv][outa]");


        // Hoàn thành builder
        builder.setComplexFilter(filterBuilder.toString())
                .addOutput(outputUrl)
                .setVideoCodec("libx264")
                .setVideoBitRate(2_000_000)
                .setAudioCodec("aac")
                .setFormat("flv")
                .addExtraArgs("-map", "[outv]")
                .addExtraArgs("-map", "[outa]")
                .done();

        // Tạo executor để thực thi lệnh
        FFmpegExecutor executor = new FFmpegExecutor(getFFmpeg());

        streamInfo.setOrder(-1);
        streamInfo.setEvent(StreamEvent.START_STREAM);
        streamInfo.raiseEvent(streamInfo.getStreamKey());

        // Thực thi lệnh
        executor.createJob(builder, new ProgressListener() {
            //Chuẩn bị thông tin cần thiết:
            int curOrder;
            List<Double> durations = getVideosDuration(listVideo);
            double[] cumulativeDurations = VideoUtils.computeCumulativeDurations(durations);
            int preOrder = -1;

            // Using the FFmpegProbeResult determine the duration of the input
            @Override
            public void progress(Progress progress) {

                curOrder = VideoUtils.getCurrentVideoIndex(cumulativeDurations,progress.out_time_ns);
                if (curOrder != preOrder) {
                    streamInfo.setOrder(curOrder);
                    streamInfo.raiseEvent(streamInfo.getStreamKey());
                    curOrder = preOrder;
                }
                // Print out interesting information about the progress
//                System.out.println(String.format(
//                        "[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
//                        percentage * 100,
//                        progress.status,
//                        progress.frame,
//                        FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
//                        progress.fps.doubleValue(),
//                        progress.speed
//                ));

            }
        }).run();
    }
}
