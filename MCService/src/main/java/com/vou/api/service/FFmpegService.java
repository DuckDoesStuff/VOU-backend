package com.vou.api.service;

import com.vou.api.dto.stream.StreamEvent;
import com.vou.api.dto.stream.StreamInfo;
import com.vou.api.utils.VideoUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.apache.commons.lang3.math.Fraction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FFmpegService {
    static String pathToFFprobe ="ffmpeg";
    static String pathToFFmpeg = "ffprobe";
    static FFmpeg ffmpeg;
    static FFprobe ffprobe;
    @Value("${rtmp.server}")
    String rmtpServer;

    static FFmpeg getFFmpeg() throws IOException {
        if (ffmpeg == null) {
            ffmpeg = new FFmpeg("ffmpeg");
        }
        return ffmpeg;
    }

    static FFprobe getFFprobe() throws IOException {
        if (ffprobe == null) {
            ffprobe = new FFprobe("ffprobe");
        }
        return ffprobe;
    }

    public static List<Double> getVideosDuration(String[] videoUrls) {
        List<Double> durations = new ArrayList<>();
        try {
            for (String url : videoUrls) {
//                FFprobe ffprobe;
//                ffprobe = getFFprobe();
                FFmpegProbeResult probeResult =  getFFprobe().probe(url);
                double durationSeconds = probeResult.getFormat().duration;//  * TimeUnit.SECONDS.toNanos(1);
                durations.add(durationSeconds);
            }
            return durations;
        } catch (IOException e) {
            log.info("Khong tim thay log");
            throw new RuntimeException(e);
        }
    }

    public static FFmpegFormat getVideoFFmpegFormat(String videoPath) {
        try {
            FFmpegProbeResult probeResult = getFFprobe().probe(videoPath);
            return probeResult.getFormat();
        } catch (IOException e) {
            log.info("Khong tim thay log");
            throw new RuntimeException(e);
        }
    }

    public static List<FFmpegStream> getFFmpegStreams(String videoPath) {
        try {
            FFmpegProbeResult probeResult = getFFprobe().probe(videoPath);
            return probeResult.getStreams();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FFmpegStream getOnlyVideoFFmpegStream(String videoPath) {
        for (FFmpegStream stream : getFFmpegStreams(videoPath)) {
            if (FFmpegStream.CodecType.VIDEO.equals(stream.codec_type)) {
              return stream;
            }
        }
        return null;
    }


    public static double getFPS(FFmpegStream videoStream) {
        if (videoStream == null) {
            return -1;
        }
        Fraction rFrameRate = videoStream.r_frame_rate;
        double fps = (double) rFrameRate.getNumerator() / rFrameRate.getDenominator();
        return fps;
    }

    public static long getBitRate(FFmpegStream stream) {
        if (stream == null) {
            return -1;
        }
        return stream.bit_rate;
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

//        builder.readAtNativeFrameRate();

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
        builder
//                .setComplexFilter(filterBuilder.toString())
                .addOutput(outputUrl)
                .setVideoCodec("libx264")
                .setVideoBitRate(2_000_000)
                .setAudioCodec("aac")
                .setFormat("flv")
//                .addExtraArgs("-map", "[outv]")
//                .addExtraArgs("-map", "[outa]")
//                .addExtraArgs("-vsync", "1") // Đảm bảo không thay đổi tốc độ khung hình
//                .addExtraArgs("-r", "30") // Đặt tốc độ khung hình cho video đầu ra
                .done();

        // Tạo executor để thực thi lệnh
        FFmpegExecutor executor = new FFmpegExecutor(getFFmpeg());

        streamInfo.setOrder(-1);
        streamInfo.setEvent(StreamEvent.START_STREAM);
        streamInfo.raiseEvent(streamInfo.getStreamKey());
        List<Double> durations = getVideosDuration(listVideo);
        double[] cumulativeDurations = VideoUtils.computeCumulativeDurations(durations);

//        for (int i = 0; i < durations.size(); i++) {
//            log.info(durations.get(i)+ "");
//        }
        for (double cumulativeDuration : cumulativeDurations) {
            log.info("{}", cumulativeDuration);
        }
        // Thực thi lệnh
        executor.createJob(builder, new ProgressListener() {
            //Chuẩn bị thông tin cần thiết:
//            int curOrder;
//            List<Double> durations = getVideosDuration(listVideo);
//            double[] cumulativeDurations = VideoUtils.computeCumulativeDurations(durations);
//
//            int preOrder = -1;

            // Using the FFmpegProbeResult determine the duration of the input
            @Override
            public void progress(Progress progress) {
//                log.info(durations.toString());
//                log.info(cumulativeDurations.toString());
//                curOrder = VideoUtils.getCurrentVideoIndex(cumulativeDurations,progress.out_time_ns);
//                log.info(Integer.toString(curOrder));
//                if (curOrder != preOrder) {
//                    streamInfo.setOrder(curOrder);
//                    streamInfo.raiseEvent(streamInfo.getStreamKey());
//                    curOrder = preOrder;
//                }
                // Print out interesting information about the progress
                System.out.println(String.format(
                        "status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
                        progress.status,
                        progress.frame,
                        FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                        progress.fps.doubleValue(),
                        progress.speed
                ));

            }
        }).run();
        log.info("ffmpeg stream started");
    }

    public void streamVideo2(StreamInfo streamInfo) throws IOException, InterruptedException {
        String[] listVideo = streamInfo.getVideoUrl();
        String streamKey = streamInfo.getStreamKey();
        if (listVideo == null || listVideo.length == 0) {
            return;
        }
        String outputUrl = rmtpServer + "/" + streamKey;

        List<String> command = new ArrayList<>();
        command.add("ffmpeg");

        // Thêm input files
        for (String videoPath : listVideo) {
            command.add("-re");  // Thêm flag -re để đọc input với native framerate
            command.add("-i");
            command.add(videoPath);
        }

        // Thêm các tham số khác
//        command.add("-filter_complex");
//        command.add(buildComplexFilter(listVideo.length));
//        command.add("-map");
//        command.add("[outv]");
//        command.add("-map");
//        command.add("[outa]");
        command.add("-c:v");
        command.add("copy");

// Thêm mã hóa âm thanh nếu cần
        command.add("-c:a");
        command.add("aac");

        command.add("-f");
        command.add("flv");

        command.add("-flvflags");
        command.add("no_duration_filesize");  // Không ghi duration và filesize vào header FLV

        command.add("-live_start_index");
        command.add("0");  // Bắt đầu stream từ frame đầu tiên

//        command.add("-c:v");
//        command.add("libx264");
//        command.add("-preset");
//        command.add("ultrafast");  // Sử dụng preset ultrafast để giảm độ trễ
//        command.add("-b:v");
//        command.add("2000k");
//        command.add("-c:a");
//        command.add("aac");
//        command.add("-f");
//        command.add("flv");
//        command.add("-flvflags");
//        command.add("no_duration_filesize");  // Không ghi duration và filesize vào header FLV
//        command.add("-live_start_index");
//        command.add("0");  // Bắt đầu stream từ frame đầu tiên
//        command.add("-max_delay");
//        command.add("100");  // Đặt độ trễ tối đa (ms)
//        command.add("-tune");
//        command.add("zerolatency");  // Tối ưu hóa cho độ trễ thấp
        command.add(outputUrl);

//        command.add("-filter_complex");
//        command.add(buildComplexFilter(listVideo.length));
//        command.add("-map");
//        command.add("[outv]");
//        command.add("-map");
//        command.add("[outa]");
//        command.add("-c:v");
//        command.add("libx264");
//        command.add("-preset");
//        command.add("veryfast");
//        command.add("-b:v");
//        command.add("2000k");
//        command.add("-maxrate");
//        command.add("2500k");
//        command.add("-bufsize");
//        command.add("2000k");
//        command.add("-c:a");
//        command.add("aac");
//        command.add("-f");
//        command.add("flv");
//        command.add("-vsync");
//        command.add("cfr");
//        command.add("-af");
////        command.add("aresample=async=1");
//        command.add("-fflags");
//        command.add("+genpts");
//        command.add("-max_interleave_delta");
//        command.add("0");
//        command.add("-copyts");
//        command.add(outputUrl);

        log.info(command.toString());

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("frame=")) {
                // Xử lý thông tin tiến trình
                processProgress(line);
            }
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        System.out.println("FFmpeg process exited with code " + exitCode);
    }

    public void streamVideo3(StreamInfo streamInfo) throws IOException, InterruptedException {
        String[] listVideo = streamInfo.getVideoUrl();
        String streamKey = streamInfo.getStreamKey();
        if (listVideo == null || listVideo.length == 0) {
            return;
        }

    }

    private String buildComplexFilter(int videoCount) {
        StringBuilder filterBuilder = new StringBuilder();
        for (int i = 0; i < videoCount; i++) {
            filterBuilder.append("[").append(i).append(":v]scale=576:576:force_original_aspect_ratio=decrease,pad=576:576:(ow-iw)/2:(oh-ih)/2[v").append(i).append("];");
        }
        for (int i = 0; i < videoCount; i++) {
            filterBuilder.append("[v").append(i).append("][").append(i).append(":a]");
        }
        filterBuilder.append("concat=n=").append(videoCount).append(":v=1:a=1[outv][outa]");
        return filterBuilder.toString();
    }

    private void processProgress(String progressLine) {
        // Ví dụ: frame=  942 fps= 31 q=-1.0 size=    3072kB time=00:00:31.40 bitrate= 800.8kbits/s speed=1.04x
        String[] parts = progressLine.split("\\s+");
        for (String part : parts) {
            if (part.startsWith("time=")) {
                String time = part.substring(5);
                // Xử lý thời gian ở đây
                System.out.println("Current time: " + time);
                // Bạn có thể thêm logic để xử lý các mốc thời gian cụ thể ở đây
            }
        }
    }
}
