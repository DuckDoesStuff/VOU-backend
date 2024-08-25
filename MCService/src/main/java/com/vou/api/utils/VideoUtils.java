package com.vou.api.utils;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Iterator;
import java.util.List;

public class VideoUtils {

    public static double[] computeCumulativeDurations(List<Double> videoDurations) {
        double[] cumulativeDurations = new double[videoDurations.size()];
        double total = 0;
        for (int i = 0; i < videoDurations.size(); i++) {
            total += videoDurations.get(i);
            cumulativeDurations[i] = total;
        }
        return cumulativeDurations;
    }

    public static int getCurrentVideoIndex(double[] cumulativeDurations, double currentPlaybackTime) {
        int low = 0;
        int high = cumulativeDurations.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (currentPlaybackTime < cumulativeDurations[mid]) {
                if (mid == 0 || currentPlaybackTime >= cumulativeDurations[mid - 1]) {
                    return mid;
                }
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return -2; // Trường hợp thời gian phát hiện tại lớn hơn tổng thời gian của tất cả video
    }

    public static byte[] pictureToByteArray(Picture picture) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Lấy dữ liệu pixel từ Picture (giả sử là byte[][])
        byte[][] data = picture.getData();
        int height = data.length;
        int width = data[0].length;

        // Chuyển đổi mảng hai chiều thành mảng một chiều
        for (int y = 0; y < height; y++) {
            baos.write(data[y]); // Ghi từng hàng của mảng hai chiều
        }

        return baos.toByteArray();
    }

    public static Iterator<Picture> getFrames(String videoFilePath) throws IOException, JCodecException {
        SeekableByteChannel byteChannel = NIOUtils.readableChannel(new java.io.File(videoFilePath));
        FrameGrab grab = FrameGrab.createFrameGrab(byteChannel);
        return new Iterator<Picture>() {
            Picture nextPicture = null;

            @Override
            public boolean hasNext() {
                try {
                    nextPicture = grab.getNativeFrame();
                    return nextPicture != null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public Picture next() {
                return nextPicture;
            }
        };
    }

//        public static Iterator<Picture> getFrames(InputStream videoStream) throws IOException, JCodecException {
//            SeekableByteChannel byteChannel = NIOUtils.readableChannel(Channels.newChannel(videoStream));
//            FrameGrab grab = FrameGrab.createFrameGrab(byteChannel);
//            return new Iterator<Picture>() {
//                Picture nextPicture = null;
//
//                @Override
//                public boolean hasNext() {
//                    try {
//                        nextPicture = grab.getNativeFrame();
//                        return nextPicture != null;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                }
//
//                @Override
//                public Picture next() {
//                    return nextPicture;
//                }
//            };
//        }
//    }
}
