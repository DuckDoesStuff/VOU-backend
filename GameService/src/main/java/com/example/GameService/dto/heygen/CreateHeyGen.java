package com.example.GameService.dto.heygen;

import lombok.Data;



@Data
public class CreateHeyGen {
    private String error;
    private VideoData data;

    @Data
    public static class VideoData {
        private String video_id;
    }

    private String message;
}
