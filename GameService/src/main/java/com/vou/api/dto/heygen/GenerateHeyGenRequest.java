package com.vou.api.dto.heygen;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenerateHeyGenRequest {
    private List<VideoInput> video_inputs;
    private Dimension dimension;
    private String title;

    @Data
    @AllArgsConstructor
    public static class VideoInput {
        private Character character;
        private Voice voice;

        @Data
        @AllArgsConstructor
        public static class Character {
            private String type;
            private String avatar_id;
            private String avatar_style;
        }

        @Data
        @Builder
        public static class Voice {
            private String type;
            private String duration;
            private String input_text;
            private String voice_id;
        }
    }

    @Data
    @AllArgsConstructor
    public static class Dimension {
        private int width;
        private int height;
    }
}
