package MCService.utils;

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
}
