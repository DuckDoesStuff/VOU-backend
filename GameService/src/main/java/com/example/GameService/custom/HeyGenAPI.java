package com.example.GameService.custom;

import com.example.GameService.dto.heygen.CreateHeyGen;
import com.example.GameService.dto.heygen.RetrieveHeyGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HeyGenAPI {
    @Value("${ heygen.api_key }")
    private String apiKey;

    private final String createURL = "https://api.heygen.com/v2/video/generate";
    private final String retrieveURL = "https://api.heygen.com/v1/video_status.get";

    public WebClient.Builder heyGenClientBuilder() {
        return WebClient.builder();
    }


    // Not ready
    public String generateVideo(String question) {
        WebClient webClient = heyGenClientBuilder().build();

        CreateHeyGen result = webClient.post()
                .uri(createURL)
                .header("X-Api-Key", apiKey)
                .retrieve()
                .bodyToMono(CreateHeyGen.class)
                .block();

        assert result != null;
        return result.getData().getVideo_id();
    }

    public String getVideoIfCompleted(String videoID) {
        WebClient webClient = heyGenClientBuilder().build();

        RetrieveHeyGen result = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(retrieveURL)
                        .queryParam("video_id", videoID)
                        .build())
                .header("X-Api-Key", apiKey)
                .retrieve()
                .bodyToMono(RetrieveHeyGen.class)
                .block();

        assert result != null;
        return result.getData().getVideo_url();
    }
}
