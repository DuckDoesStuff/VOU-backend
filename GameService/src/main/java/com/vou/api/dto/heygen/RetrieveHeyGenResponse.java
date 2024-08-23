package com.vou.api.dto.heygen;

import lombok.Data;

@Data
public class RetrieveHeyGenResponse {
    private int code;
    private Result data;

    @Data
    public static class Result {
        private String callback_id;
        private String caption_url;
        private float duration;
        private String error;
        private String gif_url;
        private String id;
        private String status;
        private String thumbnail_url;
        private String video_url;
        private String video_url_caption;
    }

    private String message;
}
