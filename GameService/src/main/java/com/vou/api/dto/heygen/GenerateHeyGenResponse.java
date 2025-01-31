package com.vou.api.dto.heygen;

import lombok.Data;



@Data
public class GenerateHeyGenResponse {
    private String error;
    private VideoData data;

    @Data
    public static class VideoData {
        private String video_id;
    }

    private String message;
}
